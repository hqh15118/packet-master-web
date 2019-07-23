package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class SysRunArg {
    private double cpuRate;
    private double memRate;
    private int cpuCount;
    private int memCount;
}
