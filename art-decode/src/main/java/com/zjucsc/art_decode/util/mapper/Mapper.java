package com.zjucsc.art_decode.util.mapper;

public interface Mapper {
    void createMapper();
    String getIDByIPAndIOA(String ip,String ioa);
    String getIDByIPAndTypeAndPointIndex(String ip,String type,String pointIndex);
}
