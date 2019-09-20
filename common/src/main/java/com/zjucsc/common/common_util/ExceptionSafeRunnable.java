package com.zjucsc.common.common_util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * #project MLenningPro
 *
 * @author hongqianhui
 * #create_time 2019-09-11 - 19:17
 */
public abstract class ExceptionSafeRunnable<T> implements Runnable {
    private T t;
    private static Logger logger = LoggerFactory.getLogger(ExceptionSafeRunnable.class);
    public abstract void run(T t) ;
    public ExceptionSafeRunnable(){}
    public ExceptionSafeRunnable(T t){
        this.t = t;
    }
    @Override
    public void run() {
        try{
            run(t);
        }catch (RuntimeException e){
            Thread thread = Thread.currentThread();
            logger.error("ERROR Thread [name = {},priority = {},groupName = {}]" ,
                    thread.getName(),thread.getPriority(),thread.getThreadGroup().getName() , e);
        }
    }
}
