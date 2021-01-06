package com.cs.model.res;

public enum ResponseCode {
    SUCEESS(200),FAIL(500);
    private int code;

    private ResponseCode(int code){
        this.code=code;
    }
    public int getCode(){
        return code;
    }
}
