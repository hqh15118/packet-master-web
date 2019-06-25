package com.zjucsc.common.exceptions;

public class TokenNotValidException extends Exception {
    private String msg;
    public TokenNotValidException(String msg){
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
