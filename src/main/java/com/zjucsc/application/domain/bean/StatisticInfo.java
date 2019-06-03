package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StatisticInfo implements Serializable {
    private List<Integer> upload;
    private List<Integer> download;
    private List<Integer> attack;
    private List<Integer> exception;
    private List<String> timeList;
}
