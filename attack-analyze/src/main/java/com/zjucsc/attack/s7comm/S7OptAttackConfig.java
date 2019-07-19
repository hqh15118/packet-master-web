package com.zjucsc.attack.s7comm;

import com.zjucsc.attack.base.BaseOptConfig;

import java.util.List;

public class S7OptAttackConfig extends BaseOptConfig {
    private int DBnum;
    private int Byteoffset;
    private int Bitoffset;
    private boolean result;
    private String comment;

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getBitoffset() {
        return Bitoffset;
    }

    public void setBitoffset(int bitoffset) {
        Bitoffset = bitoffset;
    }

    public void setByteoffset(int byteoffset) {
        Byteoffset = byteoffset;
    }

    public int getByteoffset() {
        return Byteoffset;
    }

    public void setDBnum(int DBnum) {
        this.DBnum = DBnum;
    }

    public int getDBnum() {
        return DBnum;
    }
}
