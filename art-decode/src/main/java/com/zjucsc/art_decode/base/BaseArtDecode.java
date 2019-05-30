package com.zjucsc.art_decode.base;

import java.util.Map;

/**
 * 泛型化工艺参数
 * @param <T>
 */
public abstract class BaseArtDecode<T> implements IArtDecode {
    protected T artConfig;

    public void setArtConfig(T artConfig) {
        this.artConfig = artConfig;
    }

    @Override
    public Map<String, Float> decode(Map<String, Float> globalMap, byte[] payload, Object... obj) {
        if (artConfig!=null){
            return doDecode(globalMap, payload, obj);
        }
        return null;
    }

    public abstract Map<String,Float> doDecode(Map<String, Float> globalMap, byte[] payload, Object... obj);
}
