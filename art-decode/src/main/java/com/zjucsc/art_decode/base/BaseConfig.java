package com.zjucsc.art_decode.base;


import java.io.Serializable;

public class BaseConfig implements Serializable , Comparable<BaseConfig>{
    private String protocol;
    private String tag;
    private int showGraph;
    private int id;
    private int protocolId;
    private String details;

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
        return details;
    }

    public void setDetail(String detail) {
        this.details = detail;
    }

    @Override
    public int compareTo(BaseConfig o) {
        return this.getId() > o.getId() ? 1 : 0;
    }
}
