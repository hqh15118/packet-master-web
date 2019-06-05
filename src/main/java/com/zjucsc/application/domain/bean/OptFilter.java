package com.zjucsc.application.domain.bean;


import lombok.Data;

import java.util.List;

@Data
public class OptFilter {
    private int id;
    private String deviceNumber;
    private int funCode;
    private int protocolId;
    private int gplotId;
}
