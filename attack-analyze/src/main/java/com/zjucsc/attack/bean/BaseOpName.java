package com.zjucsc.attack.bean;

import java.util.Objects;

/**
 * opName config
 */
public class BaseOpName implements Comparable<BaseOpName>{
    private int id;
    private String protocol;
    private String opName;      //key value
    private String deviceMac;
    private boolean enable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public int compareTo(BaseOpName o) {
        if (equals(o)){
            return 0;
        }
        return opName.compareTo(o.opName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseOpName)) return false;
        BaseOpName that = (BaseOpName) o;
        return Objects.equals(getOpName(), that.getOpName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOpName());
    }
}
