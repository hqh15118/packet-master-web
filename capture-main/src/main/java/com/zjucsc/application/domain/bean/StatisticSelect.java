package com.zjucsc.application.domain.bean;


import lombok.Data;

@Data
public class StatisticSelect {

    private String deviceId;
    private String start;
    private String end;
    private String intervalType;
}
