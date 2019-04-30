package com.zjucsc.application.domain.bean;

import lombok.Data;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 23:01
 */

public class BadPacket {
    public String protocol;
    public int fun_code;
    public String operation;
    public BadPacket(String protocol){
        this.protocol = protocol;
    }
    public BadPacket(String protocol,int fun_code,String operation){
        this.protocol = protocol;
        this.fun_code = fun_code;
        this.operation = operation;
    }
}
