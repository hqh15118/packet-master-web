package com.zjucsc.application.system.entity;

import lombok.Data;

@Data
public class TimeSeriesPacket {
    private String deviceNumber;
    private String timeStamp;
    private int packetNumber;
}
