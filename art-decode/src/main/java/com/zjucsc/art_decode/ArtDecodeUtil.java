package com.zjucsc.art_decode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zjucsc.art_decode.artconfig.DNP3ConfigByTshark;
import com.zjucsc.art_decode.artconfig.IEC104ConfigTshark;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.art_decode.base.BaseConfig;
import com.zjucsc.art_decode.base.IArtEntry;
import com.zjucsc.art_decode.base.ValidPacketCallback;
import com.zjucsc.art_decode.can.CanDecode;
import com.zjucsc.art_decode.dnp3.DNP3Decode;
import com.zjucsc.art_decode.dnp3.DNP3DecodeByTshark;
import com.zjucsc.art_decode.iec101.IEC101Decode;
import com.zjucsc.art_decode.iec104.IEC104Decode;
import com.zjucsc.art_decode.iec104.IEC104DecodeByTshark;
import com.zjucsc.art_decode.mms.MMSDecode;
import com.zjucsc.art_decode.modbus.ModbusDecode;
import com.zjucsc.art_decode.opcda.OpcdaDecode;
import com.zjucsc.art_decode.opcua.OpcuaDecode;
import com.zjucsc.art_decode.pnio.PnioDecode;
import com.zjucsc.art_decode.s7comm.S7Decode;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
public final class ArtDecodeUtil {
    private static Logger logger = LoggerFactory.getLogger(ArtDecodeUtil.class);

    public static ArtData artData = new ArtData();
    public static ValidPacketCallback validPacketCallback;
    public static ElecStatusChangeCallback elecStatusChangeCallback;
    private static final HashMap<String, BaseArtDecode> ART_DECODE_CONCURRENT_HASH_MAP
            = new HashMap<>(10);

    private static IEC104DecodeByTshark iec104DecodeByTshark;
    private static DNP3DecodeByTshark dnp3DecodeByTshark;
    /**
     * 初始化
     */
    public static void init(){
        ART_DECODE_CONCURRENT_HASH_MAP.put("modbus",new ModbusDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("s7comm",new S7Decode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("pn_io",new PnioDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("104asdu",(iec104DecodeByTshark = new IEC104DecodeByTshark()));
        ART_DECODE_CONCURRENT_HASH_MAP.put("opcua",new OpcuaDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("dnp3",(dnp3DecodeByTshark = new DNP3DecodeByTshark()));
        ART_DECODE_CONCURRENT_HASH_MAP.put("mms",new MMSDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("iec101",new IEC101Decode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("dcerpc",new OpcdaDecode());
        ART_DECODE_CONCURRENT_HASH_MAP.put("can",new CanDecode());
    }

    public static String load104AndDNPMapperFile(String iec104Mapper,String dnp3Mapper){
        String info = null;
        if (iec104Mapper != null){
            IEC104DecodeByTshark.setIEC104MapperFile(iec104Mapper);
            info = "reload iec104 mapper file";
        }else{

        }
        if (dnp3Mapper != null){
            DNP3DecodeByTshark.setDNP3MapperFile(dnp3Mapper);
            info = "reload dnp3 mapper file";
        }
        if (info == null) {
            info = "reload iec104 and dnp3 mapper file";
        }
        return info;
    }

    public static String getWatchVar(String option){
        switch (option){
            case "iec_dnp_mapper":
                return JSON.toJSONString(new Map[]{
                        IEC104DecodeByTshark.readIOA2IDMapper(),
                }, SerializerFeature.PrettyFormat);
            case "iec_dnp_data":
                return JSON.toJSONString(new Map[]{
                        iec104DecodeByTshark.getIec104ResultValueMap().getIec104ResultValueMap()
                },SerializerFeature.PrettyFormat);
        }
        return "invalid option {" + option + "} valid:{iec_dnp_mapper|iec_dnp_data}";
    }

    @SuppressWarnings("unchecked")
    public static Set<BaseConfig> getArtConfig(String protocolName){
        BaseArtDecode baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(protocolName);
        return baseArtDecode.getArtConfigs();
    }

    /**
     * @param artMap 工艺参数map
     * @param payload tcp/原始数据
     * @param protocol 解析协议
     * @param layer 五元组
     * @param objs 额外信息
     */
    public static void artDecodeEntry(Map<String,Float> artMap, byte[] payload,
                                                   String protocol, FvDimensionLayer layer , Object...objs){
        IArtEntry entry = ART_DECODE_CONCURRENT_HASH_MAP.get(protocol);
        if (entry != null){
            entry.doDecode(artMap,payload,layer,objs);
        }else{
            if (logger.isDebugEnabled())
            {
                logger.debug("无法获取协议：[{}]的工艺参数解析器",protocol);
            }
        }
    }

    /**
     * 添加工艺参数解析配置
     * @param config 工艺参数配置
     */
    @SuppressWarnings("unchecked")
    public static void addArtDecodeConfig(BaseConfig config){
        BaseArtDecode baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(config.getProtocol());
        if (baseArtDecode!=null){
            if (config.getProtocolId() == 12){//UA要先移除原来的所有配置
                baseArtDecode.removeAllArtConfig();
            }
            baseArtDecode.addArtConfig(config);
        }else{
            if (logger.isDebugEnabled())
            {
                logger.debug("添加工艺参数解析配置失败[{}]，不存在协议：[{}]对应的解析器",config,config.getProtocol());
            }
        }
        artData.insertNewArtTag(config.getTag());
    }


    @SuppressWarnings("unchecked")
    public static void deleteArtConfig(BaseConfig config){
        BaseArtDecode baseArtDecode = ART_DECODE_CONCURRENT_HASH_MAP.get(config.getProtocol());
        if (baseArtDecode!=null){
            baseArtDecode.deleteArtConfig(config);
        }else{
            if (logger.isDebugEnabled())
            {
                logger.debug("删除工艺参数[{}]失败，不存在该参数对应的解析器",config);
            }
        }
        artData.removeArtTag(config.getTag());
    }

    public static void registerPacketValidCallback(ValidPacketCallback validPacketCallback){
        ArtDecodeUtil.validPacketCallback = validPacketCallback;
    }

    public static void registerElecStatusChangeCallback(ElecStatusChangeCallback elecStatusChangeCallback){
        ArtDecodeUtil.elecStatusChangeCallback = elecStatusChangeCallback;
    }

    public static final class ArtData{
        //工艺参数解析数据map，key工艺参数名称，value，工艺参数值
        private final ConcurrentHashMap<String,Float> ART_DATA_MAP = new ConcurrentHashMap<>();
        public void insertNewArtTag(String artTag){
            ART_DATA_MAP.putIfAbsent(artTag,0F);
        }
        public Map<String,Float> getArtDataMap(){
            return ART_DATA_MAP;
        }
        public void removeArtTag(String artTag)
        {
            ART_DATA_MAP.remove(artTag);
        }
    }

    /**
     * 根据协议及特定规则获取特定协议数据
     * @return 数据值
     */
    public static Float getElecDataByIpAddressAndId(String protocol,String ipAddress,String id){
        switch (protocol){
            case "iec104":
                String ioa = IEC104DecodeByTshark.getIOAByIpAndId(ipAddress,id);
                if (ioa != null){
//                    throw new RuntimeException("无法根据{" + ipAddress + "}" +"id{" +id + "}查询到对应的IOA地址，" +
//                            "【检查IEC104Mapper】");
                    return iec104DecodeByTshark.getIec104ResultValueMap().getResultValue(ipAddress,ioa);
                }
        }
        return null ;
    }


    public interface ElecStatusChangeCallback{
        void callback(String event,Object obj);
    }
}
