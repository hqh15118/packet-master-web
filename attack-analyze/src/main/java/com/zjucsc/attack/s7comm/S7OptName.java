package com.zjucsc.attack.s7comm;

import java.util.Objects;

public class S7OptName implements Comparable<S7OptName>{
    private int id;
    private String protocol;
    private String opName;
    private String deviceMac;
    private int byteOffset;
    private int bitOffset;
    private int dbNum;
    private boolean result;
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

    public int getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }

    public int getBitOffset() {
        return bitOffset;
    }

    public void setBitOffset(int bitOffset) {
        this.bitOffset = bitOffset;
    }

    public int getDbNum() {
        return dbNum;
    }

    public void setDbNum(int dbNum) {
        this.dbNum = dbNum;
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

    @Override
    public String toString() {
        return "S7OptName{" +
                "\nid=" + id +
                ", \nprotocol=" + protocol +
                ", \nOpname='" + opName + '\'' +
                ", \ndeviceMac='" + deviceMac + '\'' +
                ", \nByteoffset=" + byteOffset +
                ", \nBitoffset=" + bitOffset +
                ", \nDBnum=" + dbNum +
                ", \nresult=" + result +
                ", \nenable=" + enable +
                "\n}";
    }

    @Override
    public int compareTo(S7OptName o) {
        if (equals(o)){
            return 0;
        }
        return opName.hashCode() > o.opName.hashCode() ? 1 : -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof S7OptName)) return false;
        S7OptName s7OptName = (S7OptName) o;
        return Objects.equals(getOpName(), s7OptName.getOpName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOpName());
    }
}
