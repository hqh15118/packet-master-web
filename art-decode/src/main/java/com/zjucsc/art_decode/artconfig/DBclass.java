package com.zjucsc.art_decode.artconfig;

public class DBclass {

    private int dbnum;
    private int byteoffset;
    private int bitoffset;
    private int length;



    public int getBitoffset() {
        return bitoffset;
    }

    public int getByteoffset() {
        return byteoffset;
    }

    public int getDbnum() {
        return dbnum;
    }

    public int getLength() {
        return length;
    }

    public void setBitoffset(int bitoffset) {
        this.bitoffset = bitoffset;
    }

    public void setByteoffset(int byteoffset) {

        this.byteoffset = byteoffset;
    }

    public void setDbnum(int dbnum) {
        this.dbnum = dbnum;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
