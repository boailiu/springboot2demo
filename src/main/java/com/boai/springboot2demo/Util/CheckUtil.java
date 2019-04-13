package com.boai.springboot2demo.Util;

import java.util.regex.Pattern;

public class CheckUtil {

    public static boolean checkName(String name) {
        return Pattern.matches("^\\p{IsHan}{2,}(?:\\u00b7\\p{IsHan}+)?$", name);
    }

    public static boolean checkMobile(String mobile) {
        return Pattern.matches("^1[1-9]{2}[0-9]{8}", mobile);
    }

    public static boolean checkCertNo(String certNo){
        return Pattern.matches("\\d{17}[0-9xX]",certNo);
    }

    public static boolean checkUrl(String url){
        return Pattern.matches("^[http|https]+[://]+",url);
    }
}
