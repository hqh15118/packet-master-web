package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class TimeSeriesPacket {
    private String deviceNumber;
    private String timeStamp;
    private int packetNumber;
}
