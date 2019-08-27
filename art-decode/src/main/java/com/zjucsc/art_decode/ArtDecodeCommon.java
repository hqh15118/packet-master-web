package com.zjucsc.art_decode;

import com.oracle.webservices.internal.api.databinding.DatabindingMode;
import com.sun.javaws.CacheUtil;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.art_decode.base.ValidPacketCallback;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.base.IArtEntry;
import com.zjucsc.art_decode.dnp3.DNP3Decode;
import com.zjucsc.art_decode.iec101.IEC101Decode;
import com.zjucsc.art_decode.iec104.IEC104Decode;
import com.zjucsc.art_decode.mms.MMSDecode;
import com.zjucsc.art_decode.modbus.ModbusDecode;
import com.zjucsc.art_decode.opcda.OpcdaDecode;
import com.zjucsc.art_decode.opcua.OpcuaDecode;
import com.zjucsc.art_decode.pnio.PnioDecode;
import com.zjucsc.art_decode.s7comm.S7Decode;
import com.zjucsc.common.bean.ThreadPoolInfoWrapper;
import com.zjucsc.common.common_util.CommonUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;

public class ArtDecodeCommon {

    private static final ExecutorService ART_DECODE_SERVICE = CommonUtil.getSingleThreadPoolSizeThreadPool(10000,
            r -> {
                Thread thread = new Thread(r);
                thread.setName("-art-decode-service-thread-");
                return thread;
            },"ART_DECODE_SERVICE");

    public static List<ThreadPoolInfoWrapper> getArtDecodeServiceInfo(){
        return new ArrayList<ThreadPoolInfoWrapper>(){
            {
                add(new ThreadPoolInfoWrapper("ART_DECODE_SERVICE", ((ThreadPoolExecutor) ART_DECODE_SERVICE).getQueue().size()));
            }
        };
    }

    public static ValidPacketCallback validPacketCallback;
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
        ART_DECODE_CONCURRENT_HASH_MAP.put("iec101",new IEC104Decode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("mms",new MMSDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("iec101",new IEC101Decode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("dcerpc",new OpcdaDecode());
    }

    private static HashMap<String,Long> DECODE_DELAY_WRAPPER = new HashMap<String,Long>(){
        {
            put("modbus",0L);put("s7comm",0L);put("pn_io",0L);put("104asdu",0L);
            put("opcua",0L);put("dnp3",0L);put("iec101",0L);put("mms",0L);
        }
    };

    @SuppressWarnings("unchecked")
    public static Set<BaseConfig> getArtConfig(String protocolName){
        BaseArtDecode baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(protocolName);
        return baseArtDecode.getArtConfigs();
    }

    public static Map<String,Long> getDecodeDelayMapInfo(){
        return DECODE_DELAY_WRAPPER;
    }

    public static void artDecodeEntry(Map<String,Float> artMap, byte[] payload,
                                                   String protocol, FvDimensionLayer layer , Object...objs){
        IArtEntry entry = ART_DECODE_CONCURRENT_HASH_MAP.get(protocol);
        if (entry != null){
            long timeStart = System.currentTimeMillis();
            entry.doDecode(artMap,payload,layer,objs);
            long timeDiff = System.currentTimeMillis() - timeStart;
            DECODE_DELAY_WRAPPER.computeIfPresent(protocol, (s, aLong) -> {
                if (aLong < timeDiff) {
                    return timeDiff;
                }else{
                    return aLong;
                }
            });
        }
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
            if (config.getProtocolId() == 12){//UA要先移除原来的所有配置
                baseArtDecode.removeAllArtConfig();
            }
            baseArtDecode.addArtConfig(config);
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
