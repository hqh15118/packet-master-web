package com.zjucsc.art_decode.base;

import java.io.Serializable;
import java.util.Map;

public interface IArtDecode<T extends BaseConfig> extends Serializable {
    Map<String,Float> decode(T t ,Map<String,Float> globalMap,byte[] payload,Object...obj);
    String protocol();
}
