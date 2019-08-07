package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class PacketHistoryList {
    private int limit;
    private String timeStamp;
    private int mode;//0 时间戳之前20,1是时间戳之后20条
}
