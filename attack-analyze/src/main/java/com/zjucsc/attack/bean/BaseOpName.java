package com.zjucsc.attack.bean;

public class BaseOpName implements Comparable<BaseOpName>{
    private int id;
    private String protocol;
    private String opName;
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
        if (o == null){
            return 1;
        }
        return opName.compareTo(o.opName);
    }
}
