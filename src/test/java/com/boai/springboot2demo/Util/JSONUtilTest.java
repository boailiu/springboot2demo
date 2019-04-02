package com.boai.springboot2demo.Util;

import com.boai.springboot2demo.Model.User;
import com.boai.springboot2demo.Model.UserCond;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class JSONUtilTest {

    private static final Logger logger_ = LoggerFactory.getLogger(JSONUtilTest.class);

    @Test
    public void objectToJson() {
        User user = new User(3L, "andy", "baliu@che300.com");
        String json = JSONUtil.objectToJson(user);
        logger_.info("object to json result:" + json);
    }

    @Test
    public void objectToJson2() {
        User userCond = getUser();
        String json = JSONUtil.objectToJson2(userCond);
        logger_.info("object to json result:" + json);
    }

    public User getUser() {
        UserCond userCond = new UserCond();
        userCond.setId(4L);
        userCond.setName("boai");
        userCond.setStartDate(LocalDate.now());
        userCond.setStartTime(LocalDateTime.now());
        return userCond;
    }
}