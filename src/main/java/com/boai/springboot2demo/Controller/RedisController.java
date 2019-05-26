package com.boai.springboot2demo.Controller;

import com.boai.springboot2demo.Util.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/redis")
public class RedisController {
    private static final Logger logger = LoggerFactory.getLogger(RedisController.class);

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping
    @RequestMapping("/addKey/{key}/{value}")
    public Map<String, Object> addKey(@PathVariable("key") String key, @PathVariable("value") Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            logger.error("redis set key error", e);
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @GetMapping("/addUser")
    public Map<String, Object> addUser() {
        JSONObject user = new JSONObject();
        JSONUtil.put(user, "id", 3);
        JSONUtil.put(user, "name", "userName1");
        JSONUtil.put(user, "email", "user@123.com");
        redisTemplate.opsForValue().set("userSet:user1", user.toString());
        return new HashMap<>();
    }

    @GetMapping
    @RequestMapping("/getValue/{key}")
    public Map<String, Object> getValue(@PathVariable("key") String key) {
        Object value = redisTemplate.opsForValue().get(key);
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @GetMapping
    @RequestMapping("/getUser")
    public Map<String, Object> getUser() {
        Object user = redisTemplate.opsForValue().get("userSet:user1");
        JSONObject userObject = null;
        HashMap<String, Object> map = new HashMap<>();
        if (user instanceof String) {
            try {
                userObject = new JSONObject((String) user);
            } catch (JSONException e) {
                logger.error("解析user错误", e);
            }
        }
        if (userObject != null) {
            Iterator keys = userObject.keys();
            while (keys.hasNext()) {
                Object key = keys.next();
                map.put(key.toString(), userObject.opt(key.toString()));
            }
        }
        return map;
    }

    @GetMapping
    @RequestMapping("/loginFault/{phone}")
    public void loginFaultCount(@PathVariable String phone) {
        redisTemplate.opsForValue().setIfAbsent("userLogin:user" + phone, phone);
        if (redisTemplate.opsForValue().increment("loginFault") <= 5) {
            logger.info("user{} 可以登录", phone);
        } else if (redisTemplate.opsForValue().increment("loginFault") > 5) {
            logger.info("user{} 禁止登录", phone);
        }
    }

}
