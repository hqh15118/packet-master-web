package com.zjucsc.attack.config;

import com.zjucsc.attack.bean.BaseOptConfig;

public class PnioOptConfig extends BaseOptConfig {
    private String macaddress;
    private int byteoffset;
    private int bitoffset;
    private boolean result;
    private String comment;

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public int getByteoffset() {
        return byteoffset;
    }

    public void setByteoffset(int byteoffset) {
        this.byteoffset = byteoffset;
    }

    public int getBitoffset() {
        return bitoffset;
    }

    public void setBitoffset(int bitoffset) {
        this.bitoffset = bitoffset;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
