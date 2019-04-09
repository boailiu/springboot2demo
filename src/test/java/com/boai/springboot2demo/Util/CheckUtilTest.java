package com.boai.springboot2demo.Util;

import org.junit.Test;

import static org.junit.Assert.*;

public class CheckUtilTest {

    @Test
    public void checkName() {
        String name = "刘博爱";
        System.out.println(CheckUtil.checkName(name));
    }

    @Test
    public void checkMobile() {
        String mobile = "18810901242";
        System.out.println(CheckUtil.checkMobile(mobile));
    }
}