package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class GraphInfo {
    private String timeStamp;
    private int delay;
    private int packetIn;
    private int packetOut;
    private int exception;
    private int attack;
}
