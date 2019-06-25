package com.zjucsc.common.exceptions;

public class ArtConfigNotValidException extends Exception {

    private String msg;

    public ArtConfigNotValidException(String msg){
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
