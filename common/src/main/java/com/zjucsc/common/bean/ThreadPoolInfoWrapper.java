package com.zjucsc.common.bean;


import lombok.Data;

@Data
public class ThreadPoolInfoWrapper {
    private String threadPoolName;
    private int poolTaskSize;

    public ThreadPoolInfoWrapper(String threadPoolName, int poolTaskSize) {
        this.threadPoolName = threadPoolName;
        this.poolTaskSize = poolTaskSize;
    }
}
