package com.zjucsc.base.util;

import com.zjucsc.common.bean.CustomThreadPoolExecutor;
import com.zjucsc.common.bean.ThreadPoolInfoWrapper;

import java.util.ArrayList;
import java.util.List;

public class ThreadPoolUtil {
    private final static List<CustomThreadPoolExecutor> ALL_REGISTEED_THREAD_POOL = new ArrayList<>();

    public static synchronized void registerThreadPoolExecutor(CustomThreadPoolExecutor threadPoolExecutor){
        ALL_REGISTEED_THREAD_POOL.add(threadPoolExecutor);
    }
    public static synchronized void removeThreadPoolExecutor(CustomThreadPoolExecutor threadPoolExecutor){
        ALL_REGISTEED_THREAD_POOL.remove(threadPoolExecutor);
    }

    public static synchronized List<ThreadPoolInfoWrapper> getThreadPoolInfos(){
        List<ThreadPoolInfoWrapper> threadPoolInfoWrappers = new ArrayList<>();
        for (CustomThreadPoolExecutor threadPoolExecutor : ALL_REGISTEED_THREAD_POOL) {
            ThreadPoolInfoWrapper threadPoolWrapper = new ThreadPoolInfoWrapper(
                    threadPoolExecutor.getTag(),
                    threadPoolExecutor.getQueue().size()
            );
            threadPoolInfoWrappers.add(threadPoolWrapper);
        }
        return threadPoolInfoWrappers;
    }
}
