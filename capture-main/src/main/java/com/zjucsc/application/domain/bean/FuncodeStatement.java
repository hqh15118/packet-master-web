package com.zjucsc.application.domain.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FuncodeStatement {
    @NotBlank
    private String funCode;
    @NotBlank
    private String label;

    public FuncodeStatement(String funCode, String label) {
        this.funCode = funCode;
        this.label = label;
    }

}
