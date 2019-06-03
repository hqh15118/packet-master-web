package com.zjucsc.application.domain.bean;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StatisticInfoSaveBean implements Serializable {

    private String deviceNumber;
    private int upload;
    private int download;
    private int attack;
    private int exception;
    private String time;
}
