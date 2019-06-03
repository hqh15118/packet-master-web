package com.zjucsc.application.domain.exceptions;

import org.springframework.scheduling.annotation.Scheduled;

public class ScriptException extends Exception {
    private String msg;
    public ScriptException(String msg){
        this.msg = msg;
    }
}
