package com.zjucsc.application.domain.exceptions;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 22:18
 */
public class DeviceNotValidException extends Exception{
    private String msg;
    public DeviceNotValidException(String msg){
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
