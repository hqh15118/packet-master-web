package com.zjucsc.application.domain.bean;

import lombok.Data;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-03 - 15:01
 */

//设备采集器状态
@Data
public class CollectorState {
    //采集器ID
    private int collectorId;
    //A状态变化后上一次状态
    private int A_lastState;
    //B状态变化后上一次状态
    private int B_lastState;
    //A状态变化后本次状态
    private int A_currentState;
    //A状态变化后本次状态
    private int B_currentState;

    public CollectorState(int collectorId, int a_lastState, int b_lastState, int a_currentState, int b_currentState) {
        this.collectorId = collectorId;
        A_lastState = a_lastState;
        B_lastState = b_lastState;
        A_currentState = a_currentState;
        B_currentState = b_currentState;
    }


}
