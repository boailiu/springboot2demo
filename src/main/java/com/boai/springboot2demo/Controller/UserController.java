package com.boai.springboot2demo.Controller;

import com.boai.springboot2demo.Exception.CommonException;
import com.boai.springboot2demo.Model.User;
import com.boai.springboot2demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository uRepo;

    @Autowired
    public UserController(UserRepository uRepo) {
        this.uRepo = uRepo;
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

    @GetMapping("/testException")
    public void ex() {
        throw new CommonException(4000, "测试的公共错误");
    }


}
