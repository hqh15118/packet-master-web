package com.zjucsc.application.tshark.filter;

import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.AttackTypePro;
import com.zjucsc.application.domain.bean.FvDimensionFilter;
import com.zjucsc.application.domain.bean.Rule;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.zjucsc.application.config.PACKET_PROTOCOL.OTHER;
import static com.zjucsc.application.util.CacheUtil.PROTOCOL_STR_TO_INT;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 22:10
 */
@Slf4j
@Data
public class FiveDimensionPacketFilter {

    private static HashMap<String,String> EMPTY_MAP = new HashMap<>(0);

    private static final String DST_IP_WHITE = "dst_ip_white";
    private static final String SRC_IP_WHITE = "src_ip_white";
    private static final String DST_PORT_WHITE = "dst_port_white";
    private static final String SRC_PORT_WHITE = "src_port_white";
    private static final String PROTOCOL_WHITE = "protocol_white";
//    private static final String DST_IP_BLACK = "dst_ip_black";
//    private static final String SRC_IP_BLACK = "src_ip_black";
//    private static final String DST_PORT_BLACK = "dst_port_black";
//    private static final String SRC_PORT_BLACK = "src_port_black";
//    private static final String PROTOCOL_BLACK = "protocol_black";
    private static final String SRC_MAC_ADDRESS_WHITE = "src_mac_address_white";
//    private static final String SRC_MAC_ADDRESS_BLACK = "src_mac_address_black";
    private static final String DST_MAC_ADDRESS_WHITE = "dst_mac_address_white";
//    private static final String DST_MAC_ADDRESS_BLACK = "dst_mac_address_black";

    private Map<String,String> srcIpWhiteMap = EMPTY_MAP;
    //private HashMap<String,String> srcIpBlackMap = EMPTY_MAP;
    private Map<String,String> srcPortWhiteMap = EMPTY_MAP;
    //private HashMap<String,String> srcPortBlackMap = EMPTY_MAP;
    private Map<String,String> protocolWhiteMap = EMPTY_MAP;
    //private HashMap<String,String> protocolBlackMap = EMPTY_MAP;
    private Map<String,String> dstIpWhiteMap = EMPTY_MAP;
    //private HashMap<String,String> dstIpBlackMap = EMPTY_MAP;
    private Map<String,String> dstPortWhiteMap = EMPTY_MAP;
    //private HashMap<String,String> dstPortBlackMap = EMPTY_MAP;
    private Map<String,String> srcMacAddressWhite = EMPTY_MAP;
    //private HashMap<String,String> srcMacAddressBlack = EMPTY_MAP;
    private Map<String,String> dstMacAddressWhite = EMPTY_MAP;
    //private HashMap<String,String> dstMacAddressBlack = EMPTY_MAP;

    private String userName;
    private String filterName;
    public FiveDimensionPacketFilter(String filterName){
        this.filterName = filterName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 说是add，其实是重置=-=
     * @param rules 要重新添加到缓存中的rule
     */
    public void addRules(List<Rule> rules){
        for (Rule rule : rules) {
            addRule(rule);
        }
    }
    public void addRule(Rule rule){
        if (rule.isEnable()){
            FvDimensionFilter fvDimensionFilter = rule.getFvDimensionFilter();
            dstIpWhiteMap = checkMap(dstIpWhiteMap,fvDimensionFilter.getDstIp());
            dstMacAddressWhite = checkMap(dstMacAddressWhite,fvDimensionFilter.getDstMac());
            dstPortWhiteMap = checkMap(dstPortWhiteMap,fvDimensionFilter.getDstPort());
            srcIpWhiteMap = checkMap(srcIpWhiteMap,fvDimensionFilter.getSrcIp());
            srcMacAddressWhite = checkMap(srcMacAddressWhite,fvDimensionFilter.getSrcMac());
            srcPortWhiteMap = checkMap(srcPortWhiteMap,fvDimensionFilter.getSrcPort());
            try {
                String protocolName = CacheUtil.convertIdToName(fvDimensionFilter.getProtocolId());
                if (protocolName.startsWith("dnp3")){
                    protocolName = "dnp3";
                }
                protocolWhiteMap = checkMap(protocolWhiteMap, protocolName);
            } catch (ProtocolIdNotValidException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeRule(Rule rule){
        FvDimensionFilter filter = rule.getFvDimensionFilter();
        doRemoveRule(srcIpWhiteMap,filter.getSrcIp());
        doRemoveRule(srcMacAddressWhite,filter.getSrcMac());
        doRemoveRule(srcPortWhiteMap,filter.getSrcPort());
        doRemoveRule(dstPortWhiteMap,filter.getDstPort());
        doRemoveRule(dstIpWhiteMap,filter.getDstIp());
        doRemoveRule(dstMacAddressWhite,filter.getDstMac());
        try {
            doRemoveRule(protocolWhiteMap, CacheUtil.convertIdToName(filter.getProtocolId()));
        } catch (ProtocolIdNotValidException e) {
            e.printStackTrace();
        }
    }

    public void clearRule(){
        srcIpWhiteMap.clear();
        srcMacAddressWhite.clear();
        srcPortWhiteMap.clear();
        dstPortWhiteMap.clear();
        dstIpWhiteMap.clear();
        dstMacAddressWhite.clear();
        protocolWhiteMap.clear();
    }

    private void doRemoveRule(Map<String,String> map , String key){
        if (StringUtils.isNotBlank(key)){
            map.remove(key);
        }
    }

    private Map<String, String> checkMap(Map<String,String> map, String key){
        if (StringUtils.isBlank(key)) {
            return map;
        }
        if (map == EMPTY_MAP){
            map = new ConcurrentHashMap<>();
        }
        map.put(key,"");
        return map;
    }
    /**
     * @param filterList 前端配置的针对某个设备的五元组过滤器组
     */
    public void setFilterList(List<Rule> filterList){
        //key : 类型，【目的IP】【白】名单 value：过滤用的map
        HashMap<String,ConcurrentHashMap<String, String>> allMap = new HashMap<>();
        for (Rule rule : filterList) {
//            String str = null;
//            //白名单
//            if (rule.getFvDimensionFilter() == 0){
//                setFilterMap(allMap, fiveDimensionFilter, DST_IP_WHITE, SRC_IP_WHITE, DST_PORT_WHITE, SRC_PORT_WHITE, DST_MAC_ADDRESS_WHITE, SRC_MAC_ADDRESS_WHITE, PROTOCOL_WHITE);
//            }else{
//                //黑名单
//                setFilterMap(allMap, fiveDimensionFilter, DST_IP_BLACK, SRC_IP_BLACK, DST_PORT_BLACK, SRC_PORT_BLACK, DST_MAC_ADDRESS_BLACK, SRC_MAC_ADDRESS_BLACK, PROTOCOL_BLACK);
//            }
            if (rule.isEnable()) {
                setFilterMap(allMap, rule.getFvDimensionFilter(), rule.getDstPorts());
            }
        }

        Set<String> stringSet = allMap.keySet();
        for (String s : stringSet) {
            switch (s) {
                case DST_IP_WHITE:
                    dstIpWhiteMap = allMap.get(DST_IP_WHITE);
                    break;
                case SRC_IP_WHITE:
                    srcIpWhiteMap = allMap.get(SRC_IP_WHITE);
                    break;
                case DST_PORT_WHITE:
                    dstPortWhiteMap = allMap.get(DST_PORT_WHITE);
                    break;
                case SRC_PORT_WHITE:
                    srcPortWhiteMap = allMap.get(SRC_PORT_WHITE);
                    break;
                case PROTOCOL_WHITE:
                    protocolWhiteMap = allMap.get(PROTOCOL_WHITE);
                    break;
                case SRC_MAC_ADDRESS_WHITE:
                    srcMacAddressWhite = allMap.get(SRC_MAC_ADDRESS_WHITE);
                    break;
                case DST_MAC_ADDRESS_WHITE:
                    dstMacAddressWhite = allMap.get(DST_MAC_ADDRESS_WHITE);
                    break;
//                case DST_IP_BLACK:
//                    dstIpBlackMap = allMap.get(DST_IP_BLACK);
//                    break;
//                case SRC_IP_BLACK:
//                    srcIpBlackMap = allMap.get(SRC_IP_BLACK);
//                    break;
//                case DST_PORT_BLACK:
//                    dstPortBlackMap = allMap.get(DST_PORT_BLACK);
//                    break;
//                case SRC_PORT_BLACK:
//                    srcPortBlackMap = allMap.get(SRC_PORT_BLACK);
//                    break;
//                case PROTOCOL_BLACK:
//                    protocolBlackMap = allMap.get(PROTOCOL_BLACK);
//                    break;
//                case SRC_MAC_ADDRESS_BLACK:
//                    srcMacAddressBlack = allMap.get(SRC_MAC_ADDRESS_BLACK);
//                    break;
//                case DST_MAC_ADDRESS_BLACK:
//                    dstMacAddressBlack = allMap.get(DST_MAC_ADDRESS_BLACK);
//                    break;
                default:
                    log.error("code error in five dimension filter , not define {}", s);
                    break;
            }
        }
    }

    private void setFilterMap(HashMap<String, ConcurrentHashMap<String, String>> allMap,
                              FvDimensionFilter fiveDimensionFilter,List<String> dstPorts) {
        String str;
        if (!(str = fiveDimensionFilter.getDstIp()).equals("--")){
            doSet(allMap, FiveDimensionPacketFilter.DST_IP_WHITE,str);
        }
        if (!(str = fiveDimensionFilter.getSrcIp()).equals("--")){
            doSet(allMap, FiveDimensionPacketFilter.SRC_IP_WHITE,str);
        }
//        if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDstPort()))){
//            doSet(allMap, FiveDimensionPacketFilter.DST_PORT_WHITE,str);
//        }
        if (dstPorts!=null) {
            for (String dstPort : dstPorts) {
                if (StringUtils.isNotBlank(dstPort)) {
                    doSet(allMap, FiveDimensionPacketFilter.DST_PORT_WHITE, dstPort);
                }
            }
        }
        if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrcPort()))){
            doSet(allMap, FiveDimensionPacketFilter.SRC_PORT_WHITE,str);
        }
        if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDstMac()))){
            doSet(allMap, FiveDimensionPacketFilter.DST_MAC_ADDRESS_WHITE,str);
        }
        if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrcMac()))){
            doSet(allMap, FiveDimensionPacketFilter.SRC_MAC_ADDRESS_WHITE,str);
        }
        if (fiveDimensionFilter.getProtocolId() != 0){
            String var = PROTOCOL_STR_TO_INT.get(fiveDimensionFilter.getProtocolId());
            /*
             * 如果添加的协议ID，map中无法找到对应的协议，那么就加other
             */
            if (var == null){
                doSet(allMap, FiveDimensionPacketFilter.PROTOCOL_WHITE,OTHER);
            }else{
                if (var.startsWith("dnp3")){
                    var = "dnp3";
                }
                doSet(allMap, FiveDimensionPacketFilter.PROTOCOL_WHITE, var);
            }
        }
    }

    /**
     * @param allMap
     * @param type 类型，如目的IP白名单
     * @param str 信息，如目的IP、目的MAC地址等
     */
    private void doSet(HashMap<String,ConcurrentHashMap<String,String>> allMap,String type , String str){
        allMap.putIfAbsent(type , new ConcurrentHashMap<>());
        allMap.get(type).put(str , str);
    }

    public AttackBean OK(FvDimensionLayer layer){
        String typeProtocol = PacketDecodeUtil.getPacketDetailProtocol(layer);
        if (!layer.ip_dst[0].equals("--") && !srcIpWhiteMap.containsKey(layer.ip_src[0])){
            return getBadPacket(layer,AttackTypePro.VISIT_DEVICE,layer.ip_src[0]);//目的IP不在白名单
        }
//        if (!dstMacAddressWhite.containsKey(layer.eth_dst[0])){
//            return getBadPacket(layer,"非授权访问设备");//目的mac不在白名单
//        }
//        if (!layer.ip_src[0].equals("--") && !srcIpWhiteMap.containsKey(layer.ip_src[0])){
//            return getBadPacket(layer,"非授权访问设备");//源ip不在白名单
//        }
        if (!srcMacAddressWhite.containsKey(layer.eth_src[0])){
            return getBadPacket(layer,AttackTypePro.VISIT_DEVICE,layer.eth_src[0]);//源mac不在白名单
        }
        if (!protocolWhiteMap.containsKey(typeProtocol)){
            return getBadPacket(layer,AttackTypePro.VISIT_PROTOCOL,layer.protocol);//协议不在白名单
        }

//        if (!srcPortWhiteMap.containsKey(layer.src_port[0])){
//            return getBadPacket(layer,"源端口不在白名单中",DangerLevel.DANGER);
//        }
//        if (!dstPortWhiteMap.containsKey(layer.dst_port[0])){
//            return getBadPacket(layer,"目的端口不在白名单中",DangerLevel.DANGER);
//        }
        return null;
    }

//    public AttackBean ERROR(FvDimensionLayer layer){
//        if (protocolBlackMap.containsKey(layer.protocol)){
//            return getBadPacket(layer, "黑名单协议");
//        }
//        if (srcPortBlackMap.containsKey(layer.src_port[0])){
//            return getBadPacket(layer, "黑名单源端口");
//        }
//        if (dstPortBlackMap.containsKey(layer.dst_port[0])){
//            return getBadPacket(layer, "黑名单目的端口");
//        }
//        if (srcIpBlackMap.containsKey(layer.ip_src[0])){
//            return getBadPacket(layer, "黑名单源IP");
//        }
//        if (dstIpBlackMap.containsKey(layer.ip_dst[0])){
//            return getBadPacket(layer, "黑名单目的IP");
//        }
//        if (dstMacAddressBlack.containsKey(layer.eth_dst[0])){
//            return getBadPacket(layer, "黑名单目的MAC");
//        }
//        if (srcMacAddressBlack.containsKey(layer.eth_src[0])){
//            return getBadPacket(layer, "黑名单源MAC");
//        }
//        return null;
//    }

    private AttackBean getBadPacket(FvDimensionLayer layer, String attackType,String detail) {
        return new AttackBean.Builder()
                .fvDimension(layer)
                .attackType(attackType)
                .attackInfo(detail).build();
    }

    @Override
    public String toString() {
        return "FiveDimensionPacketFilter{" +
                "srcIpWhiteMap=" + srcIpWhiteMap +
                ", srcPortWhiteMap=" + srcPortWhiteMap +
                ", protocolWhiteMap=" + protocolWhiteMap +
                ", dstIpWhiteMap=" + dstIpWhiteMap +
                ", dstPortWhiteMap=" + dstPortWhiteMap +
                '}';
    }
}
