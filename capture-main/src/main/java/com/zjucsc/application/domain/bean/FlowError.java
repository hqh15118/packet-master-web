package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class FlowError {
    private String checkTime;
    private int flow;

    public FlowError(String checkTime, int flow) {
        this.checkTime = checkTime;
        this.flow = flow;
    }
}
