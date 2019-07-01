package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.Objects;

@Data
public class RightPacketInfo {
    private String protocol;
    private String src_ip;
    private String dst_ip;
    private String src_mac;
    private String dst_mac;
    private String funCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RightPacketInfo that = (RightPacketInfo) o;
        return funCode.equals(that.funCode)  &&
                protocol.equals(that.protocol) &&
                src_ip.equals(that.src_ip) &&
                dst_ip.equals(that.dst_ip) &&
                src_mac.equals(that.src_mac) &&
                dst_mac.equals(that.dst_mac);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, src_ip, dst_ip, src_mac, dst_mac, funCode);
    }
}
