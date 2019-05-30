package com.zjucsc.application.system.art;

import com.zjucsc.application.system.entity.ArtConfig;
import com.zjucsc.application.util.CommonUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.zjucsc.application.config.Common.ART_DECODE_MAP;


@Service
public class CommonArtDecodeImpl implements ICommonArtDecode {
    @Override
    public Map<String, Float> decode(Map<String, Float> globalMap, byte[] payload, String protocol) {
        ConcurrentHashMap<Integer, ArtConfig> map = ART_DECODE_MAP.get(protocol);
        if (map == null){
            //未定义该协议的payload解析
            return globalMap;
        }
        map.forEach(CommonUtil.getThreadLocalBiConsumer().setPayload(payload));
        return globalMap;
    }

}
