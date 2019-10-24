package com.zjucsc.attack.opname;

import com.zjucsc.attack.bean.BaseOpName;

public class PnioOpName extends BaseOpName {
    private int byteOffset;
    private int bitOffset;
    private boolean result;


    public int getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
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
