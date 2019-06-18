package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;

public class PnioConfig extends BaseConfig {

    private byte[] macaddress;
    private int byteoffset;
    private int bitoffset;
    private int length;
    private String type;
    private float[] range;

    public int getBitoffset() {
        return bitoffset;
    }

    public int getByteoffset() {
        return byteoffset;
    }

    public byte[] getMacaddress() {
        return macaddress;
    }

    public String getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public float[] getRange() {
        return range;
    }

    public void setRange(float[] range) {
        this.range = range;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBitoffset(int bitoffset) {
        this.bitoffset = bitoffset;
    }

    public void setByteoffset(int byteoffset) {
        this.byteoffset = byteoffset;
    }

    public void setMacaddress(byte[] macaddress) {
        this.macaddress = macaddress;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
