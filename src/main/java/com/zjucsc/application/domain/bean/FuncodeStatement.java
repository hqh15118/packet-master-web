package com.zjucsc.application.domain.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FuncodeStatement {
    @NotBlank
    private int id;
    @NotBlank
    private String label;

    public FuncodeStatement(int id, String label) {
        this.id = id;
        this.label = label;
    }
}
