package com.boai.springboot2demo.Controller;

import com.boai.springboot2demo.Exception.CommonException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public Map<String, Object> CommonExHandler(CommonException e) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", e.getErrorCode());
        result.put("msg", e.getMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> exHandler(Exception e) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", 4000);
        result.put("msg", e.getMessage());
        return result;
    }
}
