package com.zjucsc.attack.s7comm;

//import com.zjucsc.art_decode.base.BaseConfig;

import com.zjucsc.attack.bean.BaseConfig;

public class S7Config extends BaseConfig {
    private String type;
    private int database;
    private int byteoffset;
    private int bitoffset;
    private int length;

    public void setLength(int length) {
        this.length = length;
    }

    public void setByteoffset(int byteoffset) {
        this.byteoffset = byteoffset;
    }

    public void setBitoffset(int bitoffset) {
        this.bitoffset = bitoffset;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getLength() {
        return length;
    }

    public int getByteoffset() {
        return byteoffset;
    }

    public int getBitoffset() {
        return bitoffset;
    }

    public int getDatabase() {
        return database;
    }

    public String getType() {
        return type;
    }
}
