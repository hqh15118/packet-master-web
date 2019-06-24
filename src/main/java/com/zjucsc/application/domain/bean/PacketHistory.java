package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class PacketHistory {
    private List<String> time;
    private List<Integer> data;
}
