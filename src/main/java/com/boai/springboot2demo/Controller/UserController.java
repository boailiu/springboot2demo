package com.boai.springboot2demo.Controller;

import com.boai.springboot2demo.Model.User;
import com.boai.springboot2demo.Repository.UserRepository;
import com.boai.springboot2demo.Service.UserService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository uRepo;
    private UserService uService;
    private AmqpTemplate amqpTemplate;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String userName;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Autowired
    public UserController(UserRepository uRepo, UserService userService, AmqpTemplate amqpTemplate) {
        this.uRepo = uRepo;
        this.uService = userService;
        this.amqpTemplate = amqpTemplate;
    }


    @RequestMapping("/{id}")
    public User getUser(@PathVariable("id") long id) {
//        return new User(1L,"testName","18810901242@163.com");
        User user = uRepo.getUserById(id);
        return user;
    }

    @RequestMapping("/saveUser")
    public void saveUser(User user) {
        user.setId(2L);
        user.setName("name2");
        user.setEmail("test@163.com");
        uRepo.saveOrUpdateUser(user);
    }

    @GetMapping("/getUserMap/{userId}")
    public Map<String, Object> getUserMap(@PathVariable("userId") long userId) {
        return uService.getUserMap(userId);
    }

    @GetMapping("/userList")
    public Map<String, Object> getUserList() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userList", uService.listUser());
        return map;
    }

    @GetMapping("/search/{name}")
    public Map<String, Object> search(@PathVariable("name") String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user", uService.getUserMapByName(name));
        return map;
    }

    @GetMapping("testVoid")
    public void testVoid() {

    }

    @GetMapping("/testException")
    public void ex() throws Exception {
//        throw new CommonException(4000, "测试的公共错误");
        throw new Exception("测试的公共错误", new Throwable());
    }

    /**
     * 发送方确认模式
     * 接收方确认模式
     *
     * @param message
     */
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable("message") String message) {
//        amqpTemplate.convertAndSend("bootTestQueue", message);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setHost(host);
        factory.setPort(port);

        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("bootTestQueue", true, false, false, null);

            channel.confirmSelect();
            channel.basicPublish("", "bootTestQueue", null, message.getBytes("UTF-8"));
            if (channel.waitForConfirms()) {
                System.out.println("消息成功发送...");
            }
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
