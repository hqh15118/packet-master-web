package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class RuleEnable {
    private String deviceNumber;
    private String fvId;
    private boolean enable;
}
