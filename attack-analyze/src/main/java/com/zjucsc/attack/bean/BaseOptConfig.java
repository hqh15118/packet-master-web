package com.zjucsc.attack.bean;


import java.util.List;

public class BaseOptConfig implements Comparable<BaseOptConfig>{

    private int id;
    private int protocolId;
    private List<String> expression;
    private String Opname;
    private List<ArtOptWrapper> rule;
    private boolean enable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(int protocolId) {
        this.protocolId = protocolId;
    }

    public String getOpname() {
        return Opname;
    }

    public void setOpname(String opname) {
        Opname = opname;
    }

    public List<String> getExpression() {
        return expression;
    }

    public void setExpression(List<String> expression) {
        this.expression = expression;
    }

    @Override
    public int compareTo(BaseOptConfig o) {
        if (o.getOpname().equals(this.getOpname())){
            return 0;
        }
        return o.getOpname().hashCode() > o.getOpname().hashCode() ? 1 : -1;
    }

    public List<ArtOptWrapper> getRule() {
        return rule;
    }

    public void setRule(List<ArtOptWrapper> rule) {
        this.rule = rule;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
