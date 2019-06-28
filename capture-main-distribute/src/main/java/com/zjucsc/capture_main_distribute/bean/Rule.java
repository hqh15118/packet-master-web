package com.zjucsc.capture_main_distribute.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
public class Rule{
    private FvDimensionFilter fvDimensionFilter;
    private List<Integer> funCodes;
}
