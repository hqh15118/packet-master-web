package com.zjucsc.attack.bean;

import java.util.List;

public class ArtAttackAnalyzeConfig implements Comparable<ArtAttackAnalyzeConfig>{
    private List<String> expression;
    private String description;
    private boolean enable;

    public ArtAttackAnalyzeConfig(List<String> expression, String description,
                                  boolean enable) {
        this.expression = expression;
        this.description = description;
        this.enable = enable;
    }

    public List<String> getExpression() {
        return expression;
    }

    public void setExpression(List<String> expression) {
        this.expression = expression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public int compareTo(ArtAttackAnalyzeConfig o) {
        return o.getDescription().hashCode() > this.getDescription().hashCode() ? 1:-1;
    }
}
