package com.zjucsc.application.domain.exceptions;

public class ProtocolIdNotValidException extends Exception{
    private String msg;
    public ProtocolIdNotValidException(String msg){
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
