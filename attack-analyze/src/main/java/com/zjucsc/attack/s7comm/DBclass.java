package com.zjucsc.attack.s7comm;

public class DBclass {

    private int dbnum;
    private int byteoffset;
    private int bitoffset;
    private int length;
    private byte[] data;
    private int transportsize;//2为byte,3为bit


    public int getTransportsize() {
        return transportsize;
    }

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

    public byte[] getData() {
        return data;
    }

    public void setBitoffset(int bitoffset) {
        this.bitoffset = bitoffset;
    }

    public void setByteoffset(int byteoffset) {

        this.byteoffset = byteoffset;
    }

    public void setTransportsize(int transportsize) {
        this.transportsize = transportsize;
    }

    public void setDbnum(int dbnum) {
        this.dbnum = dbnum;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
