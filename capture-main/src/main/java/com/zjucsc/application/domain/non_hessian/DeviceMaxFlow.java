package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class DeviceMaxFlow {
    private String deviceNumber;
    private int maxFlowIn;
    private int maxFlowOut;

    public DeviceMaxFlow(){}
    public DeviceMaxFlow(String deviceNumber, int maxFlowIn, int maxFlowOut) {
        this.deviceNumber = deviceNumber;
        this.maxFlowIn = maxFlowIn;
        this.maxFlowOut = maxFlowOut;
    }
}
