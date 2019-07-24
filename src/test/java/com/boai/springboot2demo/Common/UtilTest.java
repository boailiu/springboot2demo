package com.boai.springboot2demo.Common;

public class UtilTest {

    public static void main(String[] args) {
        String url = "http://mobsec-dianhua.baidu.com/dianhua_api/open/location?tel=13806545475";
        System.out.println(url.matches("https?://.*"));

    }
}
