package com.zjucsc.common.common_util;

import com.zjucsc.common.bean.ThreadPoolInfoWrapper;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.*;

public class CommonUtil {
    private static final ThreadLocal<StringBuilder> GLOBAL_THREAD_LOCAL_STRING_BUILDER =
            new ThreadLocal<StringBuilder>(){
                @Override
                protected StringBuilder initialValue() {
                    return new StringBuilder(100);
                }
            };

    public static StringBuilder getGlobalStringBuilder(){
        StringBuilder sb = GLOBAL_THREAD_LOCAL_STRING_BUILDER.get();
        return sb.delete(0,sb.length());
    }

    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL
            = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };
    public static SimpleDateFormat getDateFormat(){
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL.get();
    }

    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL2
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmssSS"));
    public static SimpleDateFormat getDateFormat2(){
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL2.get();
    }
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL3
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS"));
    public static SimpleDateFormat getDateFormat3(){
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL3.get();
    }

    private static RejectedExecutionHandler REJECT_EXECUTION_HANDLER;
    public static ThreadPoolExecutor getFixThreadPoolSizeThreadPool(int poolSize , ThreadFactory threadFactory,RejectedExecutionHandler executionHandler){
        return new ThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                threadFactory ,
                executionHandler);
    }

    public static ThreadPoolExecutor getSingleThreadPoolSizeThreadPool(int poolSize , ThreadFactory threadFactory){
        if (REJECT_EXECUTION_HANDLER == null){
            return new ThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                    threadFactory,
                    (r, executor) -> {

                    });
        }else{
            return new ThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                    threadFactory,
                    REJECT_EXECUTION_HANDLER);
        }
    }

    public static synchronized void registerExceptionHandler(RejectedExecutionHandler executionHandler){
        if (REJECT_EXECUTION_HANDLER == null){
            REJECT_EXECUTION_HANDLER = executionHandler;
        }
    }
}
