package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NetworkInterface implements Serializable {
    private String osSmartName;
    private String description;
    private String macAddress;
    private String isUp;
    private List<String> ipAddressed;
    private String deviceRealName;
}
