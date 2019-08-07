package com.zjucsc.art_decode.base;


import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

public interface IArtEntry {
    default void doDecode(Map<String,Float> map , byte[] payload, FvDimensionLayer layer , Object...objs){

    }
}
