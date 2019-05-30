package com.zjucsc.application.system.art;

import com.zjucsc.application.system.entity.ArtConfig;
import com.zjucsc.application.util.ArtDecodeUtil;

import java.util.Map;
import java.util.function.BiConsumer;

public class CommonBiConsumer implements BiConsumer<Integer, ArtConfig> {

    private byte[] payload;
    private Map<String,Float> map;
    public CommonBiConsumer(Map<String,Float> map){
        this.map = map;
    }

    @Override
    public void accept(Integer artConfigId, ArtConfig artConfig) {
        if (artConfig.getMinLength() >= payload.length){
            if (artConfig.getArtType() == ArtConfig.ART_TYPE_BOOL){
                //开关量分析
                map.put(artConfig.getTag(),
                        ArtDecodeUtil.byteToBit(payload,artConfig.getOffset(),artConfig.getBitBucket()));
            }else if (artConfig.getArtType() == ArtConfig.ART_TYPE_CONT){
                //连续量分析
                map.put(artConfig.getTag(),
                        ArtDecodeUtil.fourByteArrToFloat(payload,artConfig.getOffset()));
            }else{
                //
            }
        }
    }

    public CommonBiConsumer setPayload(byte[] payload){
        this.payload = payload;
        return this;
    }
}
