package com.zjucsc.capture_main_distribute.bean;


import lombok.Data;

import java.io.Serializable;

@Data
public class StatisticInfoSaveBean implements Serializable {

    private String deviceNumber;
    private int upload;
    private int download;
    private int attack;
    private int exception;
    private String time;
}
