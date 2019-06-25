package com.zjucsc.common.exceptions;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 13:45
 */
public class ConfigurationNotValidException extends RuntimeException{
    public ConfigurationNotValidException(String msg){
        super(msg);
    }
}
