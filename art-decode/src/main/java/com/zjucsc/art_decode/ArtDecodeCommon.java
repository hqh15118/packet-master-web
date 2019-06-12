package com.zjucsc.art_decode;

import com.zjucsc.art_decode.artconfig.BaseConfig;
import com.zjucsc.art_decode.artdecoder.ModbusArtDecoder;
import com.zjucsc.art_decode.artdecoder.ModbusDecode;
import com.zjucsc.art_decode.artdecoder.S7CommArtDecoder;
import com.zjucsc.art_decode.artdecoder.S7Decode;
import com.zjucsc.art_decode.base.BaseArtDecode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ArtDecodeCommon {
    private static final ConcurrentHashMap<String, BaseArtDecode> ART_DECODE_CONCURRENT_HASH_MAP
            = new ConcurrentHashMap<>(5);
    /**
     * 初始化
     */
    public static void init(){
        ART_DECODE_CONCURRENT_HASH_MAP.put("modbus",new ModbusArtDecoder(new ModbusDecode()));
        ART_DECODE_CONCURRENT_HASH_MAP.put("s7comm",new S7CommArtDecoder(new S7Decode()));
    }

    public static Map<String,Float> artDecodeEntry(Map<String,Float> artMap,byte[] payload,
                                                   String protocol,Object...objs){
        BaseArtDecode<?,?> baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(protocol);
        if (baseArtDecode != null){
            return baseArtDecode.decode(artMap,payload,objs);
        }
        return artMap;
    }

    /**
     * 添加工艺参数配置
     * @param config
     * @return
     */
    @SuppressWarnings("unchecked")
    public static void addArtDecodeConfig(BaseConfig config){
        BaseArtDecode baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(config.getProtocol());
        if (baseArtDecode!=null){
            baseArtDecode.addArtConfig(config);
        }
    }

    @SuppressWarnings("unchecked")
    public static void updateArtDecodeConfig(BaseConfig config){
        BaseArtDecode baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(config.getProtocol());
        if (baseArtDecode!=null){
            baseArtDecode.updateArtConfig(config);
        }
    }

    @SuppressWarnings("unchecked")
    public static void deleteArtConfig(BaseConfig config){
        BaseArtDecode baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(config.getProtocol());
        if (baseArtDecode!=null){
            baseArtDecode.deleteArtConfig(config);
        }
    }
}
