package com.zjucsc.attack.iec104;

import com.zjucsc.attack.base.BaseOptConfig;

public class iec104Opconfig extends BaseOptConfig {
    private String Opname;
    private int category;
    private int setIOAAddress;
    private boolean result;
    private boolean optype;
    private String comment;

    public void setOpname(String opname) { this.Opname = opname; }
    public void setCategory(int category) {
        this.category = category;
    }
    public void setSetIOAAddress(int setIOAAddress) {
        this.setIOAAddress = setIOAAddress;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
    public void setOpType(boolean optype) { this.optype = optype; }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOpname() {
        return Opname;
    }
    public int getCategory() { return category; }
    public int getSetIOAAddress() { return setIOAAddress; }
    public boolean getResult() { return result; }
    public boolean getOpType() { return optype; }
    public String getComment() {
        return comment;
    }

}
