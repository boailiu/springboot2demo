package com.boai.springboot2demo.Exception;

public class CommonException extends RuntimeException {

    private Integer errorCode;
    private String message;

    public CommonException(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
