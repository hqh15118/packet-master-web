package com.zjucsc.application.domain.bean;

import lombok.Data;


import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ArtHistoryForFront {

    private List<String> timeList;
    private List<String> nameList;
    private Map<String, Map<String,List<String>>> dataOrz;
}
