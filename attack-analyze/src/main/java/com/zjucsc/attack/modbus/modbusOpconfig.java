package com.zjucsc.attack.modbus;


public class modbusOpconfig {

    private String Opname;
    private int reg;//0线圈1是保持寄存器
    private int address;
    private int bitoffset;
    private boolean result;
    private String comment;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setBitoffset(int bitoffset) {
        this.bitoffset = bitoffset;
    }

    public int getBitoffset() {
        return bitoffset;
    }

    public int getAddress() {
        return address;
    }

    public int getReg() {
        return reg;
    }

    public String getOpname() {
        return Opname;
    }

    public void setAddress(int addresshead) {
        this.address = addresshead;
    }

    public void setOpname(String opname) {
        Opname = opname;
    }

    public void setReg(int reg) {
        this.reg = reg;
    }
}
