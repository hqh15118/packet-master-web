package com.zjucsc.art_decode.base;

import com.zjucsc.art_decode.ArtDecodeCommon;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 泛型化工艺参数
 * @param <T>
 */
public abstract class BaseArtDecode<T extends BaseConfig> implements IArtDecode<T> , IArtEntry{

    private ConcurrentSkipListSet<T> configs =
            new ConcurrentSkipListSet<>();

    protected void callback(String artName,float value,FvDimensionLayer layer){
        ArtDecodeCommon.validPacketCallback.callback(artName, value, layer);
    }

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

    public void removeAllArtConfig(){
        configs = new ConcurrentSkipListSet<>();
    }

    @Override
    public Map<String, Float> doDecode(Map<String, Float> map, byte[] payload, FvDimensionLayer layer,Object... objs) {
        if (configs.size() == 0){
            return map;
        }
        for (T config : configs) {
            decode(config,map,payload,layer,objs);
        }
        return map;
    }
}
