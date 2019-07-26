package com.zjucsc.tshark.exception;

public class TsharkProcessNotOpenException extends RuntimeException{
    private String msg;
    public TsharkProcessNotOpenException(String msg){
        this.msg = msg;
    }
}
