package com.zjucsc.art_decode;

import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.art_decode.iec104.IEC104Decode;
import com.zjucsc.art_decode.modbus.ModbusDecode;
import com.zjucsc.art_decode.opcua.OpcuaDecode;
import com.zjucsc.art_decode.pnio.PnioDecode;
import com.zjucsc.art_decode.s7comm.S7Decode;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.base.IArtEntry;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ArtDecodeCommon {
    private static final ConcurrentHashMap<String, BaseArtDecode> ART_DECODE_CONCURRENT_HASH_MAP
            = new ConcurrentHashMap<>(10);
    /**
     * 初始化
     */
    public static void init(){
        ART_DECODE_CONCURRENT_HASH_MAP.put("modbus",new ModbusDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("s7comm",new S7Decode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("pn_io",new PnioDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("iec104asdu",new IEC104Decode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("opcua",new OpcuaDecode());
    }

    public static Map<String,Float> artDecodeEntry(Map<String,Float> artMap,byte[] payload,
                                                   String protocol,Object...objs){
        IArtEntry entry = ART_DECODE_CONCURRENT_HASH_MAP.get(protocol);
        if (entry != null){
            return entry.doDecode(artMap,payload,objs);
        }
        return artMap;
    }

    private static final Set<BaseConfig> ALL_ART_CONFIGS = new HashSet<>();

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
            ALL_ART_CONFIGS.add(config);
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
            ALL_ART_CONFIGS.remove(config);
        }
    }

    public static Set<BaseConfig> getAllArtConfigs(){
        return ALL_ART_CONFIGS;
    }
}
