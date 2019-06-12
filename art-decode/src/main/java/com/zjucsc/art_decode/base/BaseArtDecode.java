package com.zjucsc.art_decode.base;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 泛型化工艺参数
 * @param <T>
 */
public abstract class BaseArtDecode<T extends BaseConfig> implements IArtDecode<T> , IArtEntry{

    private final ConcurrentSkipListSet<T> configs =
            new ConcurrentSkipListSet<>();

    /**
     * 添加工艺参数配置
     * @param a 工艺参数配置
     */
    public void addArtConfig(T a){
        configs.add(a);
    }

    /**
     * update
     * @param a config
     */
    public void updateArtConfig(T a){
        configs.remove(a);
        configs.add(a);
    }

    /**
     * 删除工艺参数配置
     * @param t 工艺参数
     */
    public void deleteArtConfig(T t){
        configs.remove(t);
    }

    @Override
    public Map<String, Float> doDecode(Map<String, Float> map, byte[] payload, Object... objs) {
        if (configs.size() == 0){
            return map;
        }
        for (T config : configs) {
            decode(config,map,payload,objs);
        }
        return map;
    }
}
