package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

@Data
public class RightPacketInfo {
    private String protocol;
    private String src_ip;
    private String dst_ip;
    private String src_mac;
    private String dst_mac;
    private int funCode;
}
