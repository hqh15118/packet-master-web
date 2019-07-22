package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class ArtPacketOption {
    private List<String> artNames;
    private String timeStamp;
}
