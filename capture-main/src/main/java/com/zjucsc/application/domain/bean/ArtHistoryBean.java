package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ArtHistoryBean {
    List<String> nameList;
    private String start;
    private String end;
}
