package com.zjucsc.base.util.limit;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LimitServiceEntry<E extends Comparable>{
    private ConcurrentSkipListSet<E> concurrentSkipListSet = new ConcurrentSkipListSet<>();
    private ScheduledExecutorService scheduledExecutorService;
    private ValidInstanceCallback<E> validInstanceCallback;
    private static final int SINGLE_MODE = 1;
    private static final int MULTI_MODE = 2;
    private int mode;
    public LimitServiceEntry(ScheduledExecutorService scheduledExecutorService,
                             ValidInstanceCallback<E> validInstanceCallback,
                             int limitTime,
                             TimeUnit timeUnit){
        this.scheduledExecutorService = scheduledExecutorService;
        this.validInstanceCallback = validInstanceCallback;
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            concurrentSkipListSet.clear();
        },limitTime,limitTime, timeUnit);
        mode = SINGLE_MODE;
    }

//    public LimitServiceEntry(ScheduledExecutorService scheduledExecutorService,
//                             ValidInstanceCallback<E> validInstanceCallback,
//                             int limitNum,
//                             int limitTime,
//                             TimeUnit timeUnit){
//        this.scheduledExecutorService = scheduledExecutorService;
//        this.validInstanceCallback = validInstanceCallback;
//        scheduledExecutorService.scheduleAtFixedRate(() -> {
//            concurrentSkipListSet.clear();
//        },limitTime,limitTime, timeUnit);
//    }

    public void appendInstance(E element){
        switch (mode){
            case SINGLE_MODE :
                if (concurrentSkipListSet.add(element)){
                    validInstanceCallback.callback(element);
                }
                break;
            case MULTI_MODE :
                break;
        }
    }


}
