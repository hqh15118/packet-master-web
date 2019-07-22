package com.zjucsc.art_decode;

import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.art_decode.base.ValidPacketCallback;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.base.IArtEntry;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.HashMap;
import java.util.Map;

public class ArtDecodeCommon {

    public static ValidPacketCallback validPacketCallback;
    private static final HashMap<String, BaseArtDecode> ART_DECODE_CONCURRENT_HASH_MAP
            = new HashMap<>(10);
    /**
     * 初始化
     */
    public static void init(){
//        ART_DECODE_CONCURRENT_HASH_MAP.put("modbus",new ModbusDecode());
//        ART_DECODE_CONCURRENT_HASH_MAP.put("s7comm",new S7Decode());
//        ART_DECODE_CONCURRENT_HASH_MAP.put("pn_io",new PnioDecode());
//        ART_DECODE_CONCURRENT_HASH_MAP.put("104asdu",new IEC104Decode());
//        ART_DECODE_CONCURRENT_HASH_MAP.put("opcua",new OpcuaDecode());
//        ART_DECODE_CONCURRENT_HASH_MAP.put("dnp3",new DNP3Decode());
//        ART_DECODE_CONCURRENT_HASH_MAP.put("mms",new MMSDecode());
    }

    public static Map<String,Float> artDecodeEntry(Map<String,Float> artMap, byte[] payload,
                                                   String protocol, FvDimensionLayer layer , Object...objs){
        IArtEntry entry = ART_DECODE_CONCURRENT_HASH_MAP.get(protocol);
        if (entry != null){
            return entry.doDecode(artMap,payload,layer,objs);
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

    public static void registerPacketValidCallback(ValidPacketCallback validPacketCallback){
        ArtDecodeCommon.validPacketCallback = validPacketCallback;
    }
}
