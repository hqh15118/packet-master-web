package com.zjucsc.attack.bean;

public class AttackConfigByDevice {
    private String deviceNumber;
    private int cositeTime;
    private int mulsiteTime;
    private int cositeNum;
    private int mulsiteNum;

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public int getCositeTime() {
        return cositeTime;
    }

    public void setCositeTime(int cositeTime) {
        this.cositeTime = cositeTime;
    }

    public int getMulsiteTime() {
        return mulsiteTime;
    }

    public void setMulsiteTime(int mulsiteTime) {
        this.mulsiteTime = mulsiteTime;
    }

    public int getCositeNum() {
        return cositeNum;
    }

    public void setCositeNum(int cositeNum) {
        this.cositeNum = cositeNum;
    }

    public int getMulsiteNum() {
        return mulsiteNum;
    }

    public void setMulsiteNum(int mulsiteNum) {
        this.mulsiteNum = mulsiteNum;
    }
}
