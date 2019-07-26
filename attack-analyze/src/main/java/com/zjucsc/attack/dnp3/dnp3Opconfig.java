package com.zjucsc.attack.dnp3;


import com.zjucsc.attack.bean.BaseOptConfig;

public class dnp3Opconfig extends BaseOptConfig {
    private String Opname;
    private int category;
    private int setindex;
    private int setobjGroup;
    private boolean result;
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

    public int getSetindex() {
        return setindex;
    }

    public void setSetindex(int setindex) {
        this.setindex = setindex;
    }

    public int getSetobjGroup() {
        return setobjGroup;
    }

    public void setSetobjGroup(int setobjGroup) {
        this.setobjGroup = setobjGroup;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
