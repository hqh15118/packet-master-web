package com.zjucsc.application.domain.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CaptureService {
    public String service_ip;
    public String service_name;
    public String macAddress;
}
