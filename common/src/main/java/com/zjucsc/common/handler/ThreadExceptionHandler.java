package com.zjucsc.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 22:00
 */
public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler{
    private static Logger logger = LoggerFactory.getLogger(ThreadPoolExecutor.class);
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //logger.error("thread [{}] catch a un_handle exception",t.getName(),e);
    }
}
