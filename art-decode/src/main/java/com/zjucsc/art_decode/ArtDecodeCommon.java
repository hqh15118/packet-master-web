package com.zjucsc.art_decode;

import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.art_decode.dnp.DNP3Decode;
import com.zjucsc.art_decode.iec104.IEC104Decode;
import com.zjucsc.art_decode.modbus.ModbusDecode;
import com.zjucsc.art_decode.opcua.OpcuaDecode;
import com.zjucsc.art_decode.pnio.PnioDecode;
import com.zjucsc.art_decode.s7comm.S7Decode;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.base.IArtEntry;

import java.util.HashMap;
import java.util.Map;

public class ArtDecodeCommon {
    private static final HashMap<String, BaseArtDecode> ART_DECODE_CONCURRENT_HASH_MAP
            = new HashMap<>(10);
    /**
     * 初始化
     */
    public static void init(){
        ART_DECODE_CONCURRENT_HASH_MAP.put("modbus",new ModbusDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("s7comm",new S7Decode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("pn_io",new PnioDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("104asdu",new IEC104Decode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("opcua",new OpcuaDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("dnp3",new DNP3Decode());
    }

    public static Map<String,Float> artDecodeEntry(Map<String,Float> artMap,byte[] payload,
                                                   String protocol,Object...objs){
        IArtEntry entry = ART_DECODE_CONCURRENT_HASH_MAP.get(protocol);
        if (entry != null){
            return entry.doDecode(artMap,payload,objs);
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
