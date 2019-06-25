package com.zjucsc.application.domain.bean;

import lombok.Data;

import java.util.LinkedList;

@Data
public class GraphInfoCollection {
    private LinkedList<Integer> attack = new LinkedList<>();
    private LinkedList<Integer> exception = new LinkedList<>();
    private LinkedList<Integer> packetIn = new LinkedList<>();
    private LinkedList<Integer> packetOut = new LinkedList<>();
    private LinkedList<Integer> delay = new LinkedList<>();
    private LinkedList<String> timeStamp = new LinkedList<>();
}
