package com.zjucsc.attack.bean;

import java.util.List;

public class ArtAttackAnalyzeConfig implements Comparable<ArtAttackAnalyzeConfig>{
    private List<String> expression;
    private String description;

    public ArtAttackAnalyzeConfig(List<String> expression, String description) {
        this.expression = expression;
        this.description = description;
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

    @Override
    public int compareTo(ArtAttackAnalyzeConfig o) {
        return o.getDescription().hashCode() > this.getDescription().hashCode() ? 1:-1;
    }
}
