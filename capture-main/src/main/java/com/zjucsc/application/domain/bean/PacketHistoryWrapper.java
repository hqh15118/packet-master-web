package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.List;

@Data
public class PacketHistoryWrapper {
    private List<String> time;
    private List<Integer> data;

    public PacketHistoryWrapper(List<String> time, List<Integer> data) {
        this.time = time;
        this.data = data;
    }
}
