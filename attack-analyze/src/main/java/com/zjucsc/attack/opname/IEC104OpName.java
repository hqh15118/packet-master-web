package com.zjucsc.attack.opname;

import com.zjucsc.attack.bean.BaseOpName;

public class IEC104OpName extends BaseOpName {
    private int category;
    private int setIOAAddress;
    private int optype;
    private boolean result;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getSetIOAAddress() {
        return setIOAAddress;
    }

    public void setSetIOAAddress(int setIOAAddress) {
        this.setIOAAddress = setIOAAddress;
    }

    public int getOptype() {
        return optype;
    }

    public void setOptype(int optype) {
        this.optype = optype;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
