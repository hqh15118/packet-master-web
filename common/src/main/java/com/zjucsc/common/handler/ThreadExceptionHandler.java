package com.zjucsc.common.handler;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 22:00
 */
public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler{

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println(String.format("thread [%s] catch a unhandle exception : [%s]\n",t.getName(),e.getStackTrace()[0].toString()));
        //TODO release fix it
        e.printStackTrace();
    }
}
