package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ArtHistoryForFront {
//    private List<String> timeList;
    private List<String> nameList;
    private Map<String, Map<String,List<Float>>> dataOrz;
}
