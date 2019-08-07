package com.zjucsc.application.domain.non_hessian;

import lombok.Data;

import java.util.List;

@Data
public class ArtBean {
    private List<String> artName;
    private String startTime;
    private String endTime;
}
