package com.zjucsc.application.system.art;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.system.entity.ArtConfig;
import com.zjucsc.application.util.CommonUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CommonArtDecodeImpl implements ICommonArtDecode {
    @Override
    public Map<String, Float> decode(Map<String, Float> globalMap, byte[] payload, String protocol) {
        ConcurrentHashMap<Integer, ArtConfig> map = Common.ART_DECODE_MAP.get(protocol);
        if (map == null){
            //未定义该协议的payload解析
            return globalMap;
        }
        map.forEach(CommonUtil.getThreadLocalBiConsumer().setPayload(payload));
        return globalMap;
    }

}
