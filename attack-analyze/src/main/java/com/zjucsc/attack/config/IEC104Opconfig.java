package com.zjucsc.attack.config;

import com.zjucsc.attack.bean.BaseOptConfig;

public class IEC104Opconfig extends BaseOptConfig {
    private int category;
    private int setIOAAddress;
    private boolean result;
    private boolean optype;
    private String comment;

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
