package com.zjucsc.attack.dnp3;

import com.zjucsc.attack.base.BaseOptConfig;

public class dnp3Opconfig extends BaseOptConfig {
    private String Opname;
    private int category;
    private int setindex;
    private int setobjGroup;
    private boolean result;
    private String comment;

    public void setOpname(String opname) { this.Opname = opname; }
    public void setCategory(int category) {
        this.category = category;
    }
    public void setSetindex(int setindex) {
        this.setindex = setindex;
    }
    public void setSetobjGroup(int setobjGroup) {
        this.setobjGroup = setobjGroup;
    }
    public void setResult(boolean result) {
        this.result = result;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getOpname() {
        return Opname;
    }
    public int getCategory() { return category; }
    public int getSetindex() { return setindex; }
    public int getSetobjGroup() { return setobjGroup; }
    public boolean getResult() { return result; }
    public String getComment() {
        return comment;
    }

}
