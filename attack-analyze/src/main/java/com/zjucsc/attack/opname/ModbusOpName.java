package com.zjucsc.attack.opname;

import com.zjucsc.attack.bean.BaseOpName;


public class ModbusOpName extends BaseOpName {
    private int address;
    private int reg;
    private int bitOffset;
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

    public int getBitOffset() {
        return bitOffset;
    }

    public void setBitOffset(int bitOffset) {
        this.bitOffset = bitOffset;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
