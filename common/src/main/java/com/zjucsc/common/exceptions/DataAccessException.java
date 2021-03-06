package com.zjucsc.common.exceptions;

public class DataAccessException extends RuntimeException{
    private int code;
    private String msg;

    public DataAccessException(int code, String msg) {
        super("code : " + String.valueOf(code) + " msg : " + msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
