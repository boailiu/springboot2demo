package com.boai.springboot2demo.Util;

import com.boai.springboot2demo.Model.User;
import com.boai.springboot2demo.Model.UserCopy;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BeanUtilsTest {

    /**
     *  apache.commons.beanutils.BeanUtils
     *  可以将bean和bean直接的转换，也可以实现Map向bean，bean向map的转换
     *  性能要比spring BeanUtils效率低
     *  里面有各种各样的Converter，可以学习一下
     *  也可以实现Converter接口去扩展想要的Converter
     *
     */

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        User user = new User();
        user.setId(1L);
        user.setName("test");
        UserCopy userCopy = new UserCopy();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",2L);
        map.put("name","testName");
        map.put("createTime", LocalDateTime.now());
        map.put("createDate", LocalDate.now());
//        BeanUtils.copyProperties(map, user);
        BeanUtils.populate(userCopy, map); // map 转bean
//        BeanUtils.copyProperties(userCopy, map);
        System.out.println(userCopy.toString());
        System.out.println(map);

        Map describe = BeanUtils.describe(userCopy); // bean 转 map
        System.out.println(describe);
    }

    /**
     * Spring BeanUtils 可以实现bean向bean的转换
     * 速度快
     * 不能实现bean向map或者map向bean的转换
     */

    @Test
    public void SpringBeanUtilsTest(){
        User user = new User();
        user.setId(1L);
        user.setName("test");
        UserCopy userCopy = new UserCopy();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",2L);
        map.put("name","testName");
        map.put("createTime", LocalDateTime.now());
        map.put("createDate", LocalDate.now());
        org.springframework.beans.BeanUtils.copyProperties(map, userCopy, UserCopy.class);
        System.out.println(userCopy);
        System.out.println(user);
    }
}
