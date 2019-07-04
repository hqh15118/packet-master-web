package com.zjucsc.application.domain.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
public class Rule extends BaseResponse{
    private FvDimensionFilter fvDimensionFilter;
    private List<Integer> funCodes;
    private List<String> dstPorts;
}