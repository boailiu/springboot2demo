package com.boai.springboot2demo.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanUtils extends ApplicationObjectSupport {

    private static ApplicationContext applicationContext;

    @Autowired
    public SpringBeanUtils(ApplicationContext applicationContext) {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    public static <T> T getObject(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static Object getObject(String beanId){
        return applicationContext.getBean(beanId);
    }
}
