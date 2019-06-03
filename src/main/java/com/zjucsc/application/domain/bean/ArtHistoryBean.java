package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ArtHistoryBean {
    List<String> nameList;
    private String startTime;
    private String endTime;
    private String timeType;
}
