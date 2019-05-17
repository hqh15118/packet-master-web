package com.zjucsc.application.domain.bean;

import lombok.Data;

public class CollectorDelay {

    public String deviceNumber;
    public int delay;

    public CollectorDelay(String deviceNumber, int delay) {
        this.deviceNumber = deviceNumber;
        this.delay = delay;
    }
}
