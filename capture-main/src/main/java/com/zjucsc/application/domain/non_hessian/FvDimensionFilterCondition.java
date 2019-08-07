package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class FvDimensionFilterCondition {
    private String srcMac;
    private String srcIp;
    private String srcPort;
    private String dstMac;
    private String dstIp;
    private String dstPort;
    private String protocol;
    private String funCode;
    private String timeStart;
    private String timeEnd;
}
