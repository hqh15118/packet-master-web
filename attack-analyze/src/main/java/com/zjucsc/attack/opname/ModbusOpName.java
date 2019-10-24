package com.zjucsc.attack.opname;

import com.zjucsc.attack.bean.BaseOpName;


public class ModbusOpName extends BaseOpName {
    private int address;
    private int reg;
    private int bitoffset;
    private boolean result;

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getReg() {
        return reg;
    }

    public void setReg(int reg) {
        this.reg = reg;
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
}
