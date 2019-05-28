package com.zjucsc.application.domain.exceptions;

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
