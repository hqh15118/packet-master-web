package com.zjucsc.attack.bean;

public class DosConfig {
    private String deviceNumber;
    private int coSiteTime;
    private int coSiteNum;
    private int mulSiteTime;
    private int mulSiteNum;
    private String protocol;

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public int getCoSiteTime() {
        return coSiteTime;
    }

    public void setCoSiteTime(int coSiteTime) {
        this.coSiteTime = coSiteTime;
    }

    public int getCoSiteNum() {
        return coSiteNum;
    }

    public void setCoSiteNum(int coSiteNum) {
        this.coSiteNum = coSiteNum;
    }

    public int getMulSiteTime() {
        return mulSiteTime;
    }

    public void setMulSiteTime(int mulSiteTime) {
        this.mulSiteTime = mulSiteTime;
    }

    public int getMulSiteNum() {
        return mulSiteNum;
    }

    public void setMulSiteNum(int mulSiteNum) {
        this.mulSiteNum = mulSiteNum;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
