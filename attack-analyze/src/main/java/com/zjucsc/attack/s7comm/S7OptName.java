package com.zjucsc.attack.s7comm;

public class S7OptName {
    private int id;
    private int protocolId;
    private String Opname;
    private String deviceMac;
    private int Byteoffset;
    private int Bitoffset;
    private int DBnum;
    private boolean result;
    private boolean enable;

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

    public String getOpname() {
        return Opname;
    }

    public void setOpname(String opname) {
        Opname = opname;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public int getByteoffset() {
        return Byteoffset;
    }

    public void setByteoffset(int byteoffset) {
        Byteoffset = byteoffset;
    }

    public int getBitoffset() {
        return Bitoffset;
    }

    public void setBitoffset(int bitoffset) {
        Bitoffset = bitoffset;
    }

    public int getDBnum() {
        return DBnum;
    }

    public void setDBnum(int DBnum) {
        this.DBnum = DBnum;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
