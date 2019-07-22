package com.zjucsc.art_decode.base;

import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.io.Serializable;
import java.util.Map;

public interface IArtDecode<T extends BaseConfig> extends Serializable {
    Map<String,Float> decode(T t , Map<String,Float> globalMap, byte[] payload, FvDimensionLayer layer, Object...obj);
    String protocol();
}
