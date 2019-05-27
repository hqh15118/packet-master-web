package com.zjucsc;

public enum AttackType {
    D_DOS,
    HAZARD_ART,
    TAMPER_ATTACK;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
