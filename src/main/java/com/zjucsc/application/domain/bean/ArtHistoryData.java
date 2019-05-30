package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArtHistoryData implements Serializable {
    private String timeStamp;
    private float artValue;
    private String artName;
    private String timeType;
}
