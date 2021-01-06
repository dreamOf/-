package com.cs.exception;

public class BaseException extends RuntimeException {
    private String module;
    private int code;
    private String message;
    public BaseException(){

    }
    public BaseException(int code,String message,String module){
        this.module=module;
        this.code=code;
        this.message=message;
    }
}
