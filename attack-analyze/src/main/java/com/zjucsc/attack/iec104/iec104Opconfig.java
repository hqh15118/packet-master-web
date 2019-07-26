package com.zjucsc.attack.iec104;

import com.zjucsc.attack.bean.BaseOptConfig;

public class iec104Opconfig extends BaseOptConfig {
    private String Opname;
    private int category;
    private int setIOAAddress;
    private boolean result;
    private boolean optype;
    private String comment;

    @Override
    public String getOpname() {
        return Opname;
    }

    @Override
    public void setOpname(String opname) {
        Opname = opname;
    }

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

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isOptype() {
        return optype;
    }

    public void setOptype(boolean optype) {
        this.optype = optype;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
