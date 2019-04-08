package com.boai.springboot2demo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

    @GetMapping
    @RequestMapping("/getValue/{key}")
    public Map<String, Object> getValue(@PathVariable("key") String key) {
        Object value = redisTemplate.opsForValue().get(key);
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

}
