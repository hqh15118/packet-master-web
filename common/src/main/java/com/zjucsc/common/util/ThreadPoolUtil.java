package com.zjucsc.common.util;

import com.zjucsc.common.bean.CustomThreadPoolExecutor;
import com.zjucsc.common.bean.ThreadPoolInfoWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolUtil {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolUtil.class);

    private static RejectedExecutionHandler REJECT_EXECUTION_HANDLER;

    /**
     *
     * @param poolSize cache task max number
     * @param threadFactory thread factory
     * @param tag customer thread poolExecutor tag
     * @param executionHandler reject handler
     * @return
     */
    public static ThreadPoolExecutor getSingleThreadPoolSizeThreadPool(int poolSize , ThreadFactory threadFactory, String tag , RejectedExecutionHandler executionHandler){
        return getFixThreadPoolSizeThreadPool(1,poolSize,threadFactory,tag,executionHandler);
    }

    /**
     *
     * @param coreSize core thread size
     * @param poolSize cache task max number
     * @param threadFactory thread factory
     * @param tag customer thread poolExecutor tag
     * @param executionHandler reject handler
     * @return
     */
    public static ThreadPoolExecutor getFixThreadPoolSizeThreadPool(int coreSize , int poolSize , ThreadFactory threadFactory,String tag , RejectedExecutionHandler executionHandler){
        CustomThreadPoolExecutor customThreadPoolExecutor =  new CustomThreadPoolExecutor(coreSize, coreSize, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                threadFactory ,
                executionHandler).setTag(tag);
        return registerThreadPoolExecutor(customThreadPoolExecutor);
    }

    /**
     * use default reject handler(ignore task and log error)
     * @param coreSize core thread size
     * @param poolSize cache task max number
     * @param threadFactory thread factory
     * @param tag customer thread poolExecutor tag
     * @return
     */
    public static ThreadPoolExecutor getFixThreadPoolSizeThreadPool(int coreSize , int poolSize , ThreadFactory threadFactory,String tag){
        CustomThreadPoolExecutor customThreadPoolExecutor =  new CustomThreadPoolExecutor(coreSize, coreSize, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                threadFactory,
                (r, executor) -> logger.error("[{}] reject task : [{}]",tag,r)).setTag(tag);
        return registerThreadPoolExecutor(customThreadPoolExecutor);
    }

    /**
     *
     * @param poolSize
     * @param threadFactory
     * @param tag
     * @return
     */
    public static ThreadPoolExecutor getSingleThreadPoolSizeThreadPool(int poolSize , ThreadFactory threadFactory,String tag){
        if (REJECT_EXECUTION_HANDLER == null){
            return registerThreadPoolExecutor(new CustomThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                    threadFactory,
                    (r, executor) -> {
                        logger.error("[{}] reject task : [{}]",tag,r);
                    }).setTag(tag));
        }else{
            return registerThreadPoolExecutor(new CustomThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new LinkedBlockingQueue<>(poolSize),
                    threadFactory,
                    REJECT_EXECUTION_HANDLER).setTag(tag));
        }
    }

    public static synchronized void registerExceptionHandler(RejectedExecutionHandler executionHandler){
        if (REJECT_EXECUTION_HANDLER == null){
            REJECT_EXECUTION_HANDLER = executionHandler;
        }
    }


    private final static List<CustomThreadPoolExecutor> ALL_REGISTERED_THREAD_POOL = new ArrayList<>(10);

    public static synchronized CustomThreadPoolExecutor registerThreadPoolExecutor(CustomThreadPoolExecutor threadPoolExecutor){
        ALL_REGISTERED_THREAD_POOL.add(threadPoolExecutor);
        return threadPoolExecutor;
    }
    public static synchronized void removeThreadPoolExecutor(CustomThreadPoolExecutor threadPoolExecutor){
        ALL_REGISTERED_THREAD_POOL.remove(threadPoolExecutor);
    }

    public static synchronized List<ThreadPoolInfoWrapper> getThreadPoolInfos(){
        List<ThreadPoolInfoWrapper> threadPoolInfoWrappers = new ArrayList<>();
        for (CustomThreadPoolExecutor threadPoolExecutor : ALL_REGISTERED_THREAD_POOL) {
            ThreadPoolInfoWrapper threadPoolWrapper = new ThreadPoolInfoWrapper(
                    threadPoolExecutor.getTag(),
                    threadPoolExecutor.getQueue().size()
            );
            threadPoolInfoWrappers.add(threadPoolWrapper);
        }
        return threadPoolInfoWrappers;
    }
}
