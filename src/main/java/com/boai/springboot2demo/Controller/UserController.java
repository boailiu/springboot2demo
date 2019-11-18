package com.boai.springboot2demo.Controller;

import com.boai.springboot2demo.Model.User;
import com.boai.springboot2demo.Repository.UserRepository;
import com.boai.springboot2demo.Service.UserService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserRepository uRepo;
    private UserService uService;
    private RabbitTemplate rabbitTemplate;
    private Channel channel;

    @Autowired
    public UserController(UserRepository uRepo, UserService userService, RabbitTemplate rabbitTemplate,
                          Channel channel) {
        this.uRepo = uRepo;
        this.uService = userService;
        this.rabbitTemplate = rabbitTemplate;
        this.channel = channel;
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
        final SortedSet<Long> unConfirmSet = Collections.unmodifiableNavigableSet(new TreeSet<>());
        try {
            for (int i = 0; i < 10; i++) {
                logger.info(String.format("开始发送第 %d 条消息", i));
                message = message + " " + i;
                channel.basicPublish("", "bootTestQueue", null, message.getBytes("UTF-8"));
                //同步确认
                if (channel.waitForConfirms()) {
                    logger.info(String.format("第 %d 条消息成功发送...", i));
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/sendMessageAsyn/{message}")
    public void sendMessageAsyn(@PathVariable("message") String message) {
//        amqpTemplate.convertAndSend("bootTestQueue", message);
        final SortedSet<Long> unConfirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());

        //异步确认
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 处理确认返回成功
             * @param l 如果是多条，返回最后一条消息的tag
             * @param b 是否为多条
             */
            @Override
            public void handleAck(long l, boolean b) {
                logger.info("异步返回确认处理成功,deliverTag :" + l + " multiple : " + b);
                if (b) {
                    logger.info("multiple is true, unConfirmSet :" + unConfirmSet);
                    unConfirmSet.headSet(l + 1).clear();
                    logger.info("multiple is true,after remove unConfirmSet :" + unConfirmSet);
                } else {
                    logger.info("multiple is false,unConfirmSet :" + unConfirmSet);
                    unConfirmSet.remove(l);
                    logger.info("multiple is true,after remove unConfirmSet :" + unConfirmSet);
                }
            }

            /**
             * 处理确认返回失败
             * @param l 如果是多条，返回最后一条消息的tag
             * @param b 是否为多条
             */
            @Override
            public void handleNack(long l, boolean b) {
                logger.info("异步返回确认处理失败,deliverTag：" + l + " multiple : " + b);
                if (b) {
                    logger.info("multiple is true, unConfirmSet :" + unConfirmSet);
                    unConfirmSet.headSet(l + 1).clear();
                    logger.info("multiple is true,after remove unConfirmSet :" + unConfirmSet);
                } else {
                    logger.info("multiple is false,unConfirmSet :" + unConfirmSet);
                    unConfirmSet.remove(l);
                    logger.info("multiple is true,after remove unConfirmSet :" + unConfirmSet);
                }
            }
        });

        try {
            for (int i = 0; i < 10; i++) {
                logger.info(String.format("开始发送第 %d 条消息", i));
                message = message + " " + i;
                long tag = channel.getNextPublishSeqNo();
                logger.info(String.format("第 %d 个消息发送的 deliverTag: %d", i, tag));
                channel.basicPublish("", "bootTestQueue", MessageProperties.PERSISTENT_BASIC,
                        message.getBytes("UTF-8"));
                unConfirmSet.add(tag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }


    }

    @GetMapping("/sendMessageWithConfirm/{message}")
    public void sendMessageWithConfirm(@PathVariable("message") String message) {
        rabbitTemplate.convertAndSend("bootTestQueue2", message);
    }

    @GetMapping("/sendMessageWithExchange/{message}")
    public void sendMessageWithExchange(@PathVariable String message) {
        //匹配topic.message 和 topic.#
//        rabbitTemplate.convertAndSend("exchangeTest", "topic.message", message);
        // 只匹配topic.#
        rabbitTemplate.convertAndSend("exchangeTest", "topic.messages", message);
    }


}
