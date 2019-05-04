package com.zjucsc.application.domain.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CaptureService {
    @NotBlank
    public String service_ip;
    @NotBlank
    public String service_name;
}
