package com.cs.model.res;

public class BaseResultBean {
    private int code ;
    private String message;

    public BaseResultBean(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public BaseResultBean() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
