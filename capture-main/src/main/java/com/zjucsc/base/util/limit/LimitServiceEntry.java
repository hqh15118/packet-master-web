package com.zjucsc.base.util.limit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LimitServiceEntry<E>{
    private ConcurrentHashMap<E, AtomicInteger> map = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduledExecutorService;
    private ValidInstanceCallback<E> validInstanceCallback;
    private int limitNum;
    public LimitServiceEntry(ScheduledExecutorService scheduledExecutorService,
                             ValidInstanceCallback<E> validInstanceCallback,
                             int limitTime,
                             int limitNum,
                             TimeUnit timeUnit){
        this.scheduledExecutorService = scheduledExecutorService;
        this.validInstanceCallback = validInstanceCallback;
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            map.clear();
        },limitTime,limitTime, timeUnit);
        this.limitNum = limitNum;
    }

    public void appendInstance(E element){
        AtomicInteger num = map.putIfAbsent(element,new AtomicInteger(1));
        if (num != null){
            int currentNum = num.incrementAndGet();
            if (currentNum <= limitNum){
                validInstanceCallback.callback(element);
            }
        }
    }


    public void stopLimit(){
        scheduledExecutorService.shutdown();
    }
}
