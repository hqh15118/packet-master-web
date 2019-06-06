package com.zjucsc.tshark;

import java.util.concurrent.*;

public class TsharkErrorThreadPool extends ThreadPoolExecutor {

    public String name;

    public TsharkErrorThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,  RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        setThreadFactory(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName(name);
                return thread;
            }
        });
        setRejectedExecutionHandler(handler);
    }

    public TsharkErrorThreadPool setName(String name) {
        this.name = name;
        return this;
    }
}
