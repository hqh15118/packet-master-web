package com.zjucsc.application.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 22:00
 */
@Slf4j
public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler{

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("thread {} catch a unhandle exception : \n" , t, e);
    }
}
