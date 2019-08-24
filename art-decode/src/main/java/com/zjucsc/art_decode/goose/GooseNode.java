package com.zjucsc.art_decode.goose;

import java.util.LinkedList;
import java.util.List;

public class GooseNode {
    //连接的上一个Node
    private GooseNode prev = new GooseNode();
    //该Node连接的所有Node
    private List<GooseNode> nexts = new LinkedList<>();

    //当前Node对应的数据块原始数据
    private byte[] rawData;
    //data块类型，下面只是数据的/下面还有其他块的
    private int gooseType;
    //如果是数据块，解出来的数据
    private String data;

    public GooseNode getPrev() {
        return prev;
    }

    public void setPrev(GooseNode prev) {
        this.prev = prev;
    }

    public List<GooseNode> getNexts() {
        return nexts;
    }

    public void setNexts(List<GooseNode> nexts) {
        this.nexts = nexts;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    public int getGooseType() {
        return gooseType;
    }

    public void setGooseType(int gooseType) {
        this.gooseType = gooseType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void addNewNode(GooseNode gooseNode){
        nexts.add(gooseNode);
    }

    public static void connectTwoNode(GooseNode prev,GooseNode next){
        prev.addNewNode(next);
        next.prev = prev;
    }

    public static boolean containNode(GooseNode prev,GooseNode next){
        return prev.getNexts().contains(next);
    }


}
