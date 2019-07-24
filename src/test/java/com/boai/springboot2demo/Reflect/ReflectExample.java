package com.boai.springboot2demo.Reflect;

import com.boai.springboot2demo.Util.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectExample {

    private static final Logger logger = LoggerFactory.getLogger(ReflectExample.class);

    public static void main(String[] args) throws NoSuchMethodException {
        Class<ExcelUtil> clazz = ExcelUtil.class;
        logger.info(clazz.getName());
        logger.info(clazz.getSimpleName());
//        Method[] methods = ExcelUtil.class.getDeclaredMethods();
        Method[] methods = ExcelUtil.class.getMethods();
        for (Method method : methods) {
            logger.info(method.getName());
        }

        logger.info("修饰符:");
        int modifiers = clazz.getModifiers();
        logger.info(String.valueOf(Modifier.isPublic(modifiers)));

        Method getStringList = clazz.getDeclaredMethod("getStringList");


    }

}
