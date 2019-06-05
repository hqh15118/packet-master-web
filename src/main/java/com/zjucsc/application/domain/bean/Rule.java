package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;


@Data
public class Rule {
    private FvDimensionFilter fvDimensionFilter;
    private List<Integer> funCodes;
}
