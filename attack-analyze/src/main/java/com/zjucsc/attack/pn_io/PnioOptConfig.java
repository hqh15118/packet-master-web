package com.zjucsc.attack.pn_io;

import com.zjucsc.attack.base.BaseOptConfig;

import java.util.List;

public class PnioOptConfig extends BaseOptConfig {
    private byte[] macaddress;
    private int byteoffset;
    private int bitoffset;
    private boolean result;
    private String comment;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getBitoffset() {
        return bitoffset;
    }

    public void setBitoffset(int bitoffset) {
        this.bitoffset = bitoffset;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getByteoffset() {
        return byteoffset;
    }

    public void setByteoffset(int byteoffset) {
        this.byteoffset = byteoffset;
    }

    public void setMacaddress(byte[] macaddress) {
        this.macaddress = macaddress;
    }

    public byte[] getMacaddress() {
        return macaddress;
    }
}
