package com.zjucsc.art_decode.base;


import java.io.Serializable;
import java.util.Objects;

public class BaseConfig implements Serializable , Comparable<BaseConfig>{
    private String protocol;
    private String tag;
    private int showGraph;
    private int id;
    private int protocolId;
    private String detail;
    private String deviceMac;
    private String group;
    private String ipAddress;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getShowGraph() {
        return showGraph;
    }

    public void setShowGraph(int showGraph) {
        this.showGraph = showGraph;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public int compareTo(BaseConfig o) {
        assert o!=null;
        if (equals(o)){
            return 0;
        }
        return this.getTag().hashCode() > o.getTag().hashCode() ? 1 : -1;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseConfig)) return false;
        BaseConfig that = (BaseConfig) o;
        return Objects.equals(getTag(), that.getTag());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTag());
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
