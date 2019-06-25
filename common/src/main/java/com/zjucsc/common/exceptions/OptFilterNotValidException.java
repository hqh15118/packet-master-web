package com.zjucsc.common.exceptions;

public class OptFilterNotValidException extends Exception{
    private String msg;
    public OptFilterNotValidException(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
