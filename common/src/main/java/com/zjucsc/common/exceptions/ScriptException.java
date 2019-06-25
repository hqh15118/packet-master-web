package com.zjucsc.common.exceptions;


public class ScriptException extends Exception {
    private String msg;
    public ScriptException(String msg){
        this.msg = msg;
    }
}
