package com.zjucsc.application.domain.bean;

import lombok.Data;

@Data
public class ServiceStatus {
    private int webSocketState;
    private String interfaceName;

    public ServiceStatus(int webSocketState, String interfaceName) {
        this.webSocketState = webSocketState;
        this.interfaceName = interfaceName;
    }
}
