package com.zjucsc.art_decode.base;


import java.util.Map;

public interface IArtEntry {
    Map<String,Float> doDecode( Map<String,Float> map , byte[] payload,Object...objs);
}
