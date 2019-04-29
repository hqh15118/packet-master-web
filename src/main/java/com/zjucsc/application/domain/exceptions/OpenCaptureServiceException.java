package com.zjucsc.application.domain.exceptions;

public class OpenCaptureServiceException extends RuntimeException {

    private String msg;

    public OpenCaptureServiceException(String msg){
        super(msg);
        this.msg = msg;
    }
}
