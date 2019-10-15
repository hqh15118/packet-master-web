package com.zjucsc.common.util;

import com.zjucsc.common.bean.CustomThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.concurrent.*;

public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
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
    public static ThreadPoolExecutor getFixThreadPoolSizeThreadPool(int poolSize , ThreadFactory threadFactory,String tag , RejectedExecutionHandler executionHandler){
        return getFixThreadPoolSizeThreadPool(1,poolSize,threadFactory,tag,executionHandler);
    }

    public static ThreadPoolExecutor getFixThreadPoolSizeThreadPool(int coreSize , int poolSize , ThreadFactory threadFactory,String tag , RejectedExecutionHandler executionHandler){
        return new CustomThreadPoolExecutor(coreSize, coreSize, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                threadFactory ,
                executionHandler).setTag(tag);
    }

    public static ThreadPoolExecutor getFixThreadPoolSizeThreadPool(int coreSize , int poolSize , ThreadFactory threadFactory,String tag){
        return new CustomThreadPoolExecutor(coreSize, coreSize, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                threadFactory,
                (r, executor) -> logger.error("[{}] reject task : [{}]",tag,r)).setTag(tag);
    }

    public static ThreadPoolExecutor getSingleThreadPoolSizeThreadPool(int poolSize , ThreadFactory threadFactory,String tag){
        if (REJECT_EXECUTION_HANDLER == null){
            return new CustomThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                    threadFactory,
                    (r, executor) -> {
                        logger.error("[{}] reject task : [{}]",tag,r);
                    }).setTag(tag);
        }else{
            return new CustomThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                    threadFactory,
                    REJECT_EXECUTION_HANDLER).setTag(tag);
        }
    }

    public static synchronized void registerExceptionHandler(RejectedExecutionHandler executionHandler){
        if (REJECT_EXECUTION_HANDLER == null){
            REJECT_EXECUTION_HANDLER = executionHandler;
        }
    }
}
