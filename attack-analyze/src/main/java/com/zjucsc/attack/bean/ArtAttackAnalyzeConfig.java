package com.zjucsc.attack.bean;

import java.util.List;
import java.util.Objects;

public class ArtAttackAnalyzeConfig implements Comparable<ArtAttackAnalyzeConfig>{
    private List<String> expression;
    private String description;
    private boolean enable;

    private int id;

    public ArtAttackAnalyzeConfig(List<String> expression, String description,
                                  boolean enable,int id) {
        this.expression = expression;
        this.description = description;
        this.enable = enable;
        this.id = id;
    }

    public ArtAttackAnalyzeConfig(int id){
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(ArtAttackAnalyzeConfig o) {
        if (equals(o)) {
            return 0;
        }
        else {
            return o.id > this.id ? 1 : -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArtAttackAnalyzeConfig)) return false;
        ArtAttackAnalyzeConfig that = (ArtAttackAnalyzeConfig) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ArtAttackAnalyzeConfig{" +
                "expression=" + expression +
                ", description='" + description + '\'' +
                ", enable=" + enable +
                ", id=" + id +
                '}';
    }
}
