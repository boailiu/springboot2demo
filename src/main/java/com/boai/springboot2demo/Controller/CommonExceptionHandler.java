package com.boai.springboot2demo.Controller;

import com.boai.springboot2demo.Exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CommonExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

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
        logger.error("错误捕获", e);
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", 4000);
        result.put("msg", e.getMessage());
        return result;
    }
}
