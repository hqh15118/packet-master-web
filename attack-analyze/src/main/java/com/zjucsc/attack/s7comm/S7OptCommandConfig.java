package com.zjucsc.attack.s7comm;

import java.util.List;

public class S7OptCommandConfig implements Comparable<S7OptCommandConfig>{
    private int id;
    private String protocol;
    private String process_operate;
    private List<CommandWrapper> rule;
    private String describe;
    private boolean enable;
    private List<String> ruleString;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProcess_operate() {
        return process_operate;
    }

    public void setProcess_operate(String process_operate) {
        this.process_operate = process_operate;
    }

    public List<CommandWrapper> getRule() {
        return rule;
    }

    public void setRule(List<CommandWrapper> rule) {
        this.rule = rule;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public int compareTo(S7OptCommandConfig o) {
        return process_operate.compareTo(o.process_operate);
    }

    public List<String> getRuleString() {
        return ruleString;
    }

    public void setRuleString(List<String> ruleString) {
        this.ruleString = ruleString;
    }

    @Override
    public String toString() {
        return "S7OptCommandConfig{" +
                "\nid=" + id +
                ", \nprotocol='" + protocol + '\'' +
                ", \nprocess_operate='" + process_operate + '\'' +
                ", \nrule=" + rule +
                ", \ndescribe='" + describe + '\'' +
                ", \nenable=" + enable +
                "\n}";
    }


}
