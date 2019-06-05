package com.zjucsc.art_decode;

import com.zjucsc.art_decode.artconfig.BaseConfig;
import com.zjucsc.art_decode.artdecoder.ModbusArtDecoder;
import com.zjucsc.art_decode.artdecoder.ModbusDecode;
import com.zjucsc.art_decode.artdecoder.S7CommArtDecoder;
import com.zjucsc.art_decode.artdecoder.S7CommDecode;
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
        //ART_DECODE_CONCURRENT_HASH_MAP.put("s7comm",new S7CommArtDecoder(new S7CommDecode()));
    }

    public static Map<String,Float> artDecodeEntry(Map<String,Float> artMap,byte[] payload,
                                                   String protocol){
        BaseArtDecode<?,?> baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(protocol);
        if (baseArtDecode != null){
            return baseArtDecode.decode(artMap,payload);
        }
        return artMap;
    }

    /**
     * 添加工艺参数配置
     * @param config
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean addArtDecodeConfig(BaseConfig config){
        BaseArtDecode baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(config.protocol);
        if (baseArtDecode!=null){
            baseArtDecode.addArtConfig(config);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean deleteArtConfig(BaseConfig config){
        BaseArtDecode baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(config.protocol);
        if (baseArtDecode!=null){
            baseArtDecode.deleteArtConfig(config);
            return true;
        }
        return false;
    }
}
