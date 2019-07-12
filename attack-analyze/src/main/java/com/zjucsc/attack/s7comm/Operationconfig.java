package com.zjucsc.attack.s7comm;

import com.zjucsc.attack.base.BaseOptConfig;

import java.util.List;

public class Operationconfig extends BaseOptConfig {
    private String opname;
    private int DBnum;
    private int Byteoffset;
    private int Bitoffset;
    private boolean result;
    private List<String> expression;
    private String comment;

    public List<String> getExpression() {
        return expression;
    }

    public void setExpression(List<String> expression) {
        this.expression = expression;
    }

    public void setOpname(String opname) {
        this.opname = opname;
    }

    public String getOpname() {
        return opname;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getBitoffset() {
        return Bitoffset;
    }

    public void setBitoffset(int bitoffset) {
        Bitoffset = bitoffset;
    }

    public void setByteoffset(int byteoffset) {
        Byteoffset = byteoffset;
    }

    public int getByteoffset() {
        return Byteoffset;
    }

    public void setDBnum(int DBnum) {
        this.DBnum = DBnum;
    }

    public int getDBnum() {
        return DBnum;
    }

    public String getTechname() {
        return opname;
    }

    public void setTechname(String techname) {
        this.opname = techname;
    }
}
