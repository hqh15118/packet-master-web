package com.zjucsc.application.tshark.filter;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.DangerLevel;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.tshark.domain.bean.BadPacket;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.zjucsc.application.config.PACKET_PROTOCOL.FV_DIMENSION;
import static com.zjucsc.application.config.PACKET_PROTOCOL.OTHER;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 22:10
 */
@Slf4j
@Data
public class FiveDimensionPacketFilter {

    private static final String DST_IP_WHITE = "dst_ip_white";
    private static final String SRC_IP_WHITE = "src_ip_white";
    private static final String DST_PORT_WHITE = "dst_port_white";
    private static final String SRC_PORT_WHITE = "src_port_white";
    private static final String PROTOCOL_WHITE = "protocol_white";
    private static final String DST_IP_BLACK = "dst_ip_black";
    private static final String SRC_IP_BLACK = "src_ip_black";
    private static final String DST_PORT_BLACK = "dst_port_black";
    private static final String SRC_PORT_BLACK = "src_port_black";
    private static final String PROTOCOL_BLACK = "protocol_black";
    private static final String SRC_MAC_ADDRESS_WHITE = "src_mac_address_white";
    private static final String SRC_MAC_ADDRESS_BLACK = "src_mac_address_black";
    private static final String DST_MAC_ADDRESS_WHITE = "dst_mac_address_white";
    private static final String DST_MAC_ADDRESS_BLACK = "dst_mac_address_black";

    private HashMap<String,String> srcIpWhiteMap = new HashMap<>(0);
    private HashMap<String,String> srcIpBlackMap = new HashMap<>(0);
    private HashMap<String,String> srcPortWhiteMap = new HashMap<>(0);
    private HashMap<String,String> srcPortBlackMap = new HashMap<>(0);
    private HashMap<String,String> protocolWhiteMap = new HashMap<>(0);
    private HashMap<String,String> protocolBlackMap = new HashMap<>(0);
    private HashMap<String,String> dstIpWhiteMap = new HashMap<>(0);
    private HashMap<String,String> dstIpBlackMap = new HashMap<>(0);
    private HashMap<String,String> dstPortWhiteMap = new HashMap<>(0);
    private HashMap<String,String> dstPortBlackMap = new HashMap<>(0);
    private HashMap<String,String> srcMacAddressWhite = new HashMap<>(0);
    private HashMap<String,String> srcMacAddressBlack = new HashMap<>(0);
    private HashMap<String,String> dstMacAddressWhite = new HashMap<>(0);
    private HashMap<String,String> dstMacAddressBlack = new HashMap<>(0);

    private String userName;
    private List<FvDimensionFilter> filterList;
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

    public List<FvDimensionFilter> getFilterList(){
        return this.filterList;
    }

    /**
     * @param filterList 前端配置的针对某个设备的五元组过滤器组
     */
    public void setFilterList(List<FvDimensionFilter> filterList){
        this.filterList = filterList;
        //key : 类型，【目的IP】【白】名单 value：过滤用的map
        HashMap<String,HashMap<String, String>> allMap = new HashMap<>();
        for (FvDimensionFilter fiveDimensionFilter : filterList) {
            String str = null;
            //白名单
            if (fiveDimensionFilter.getFilterType() == 0){
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDstIp()))){
                    doSet(allMap,DST_IP_WHITE,str);//将str添加到DST_IP_WHITE map的 key// 中
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrcIp()))){
                    doSet(allMap,SRC_IP_WHITE,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDstPort()))){
                    doSet(allMap,DST_PORT_WHITE,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrcPort()))){
                    doSet(allMap,SRC_PORT_WHITE,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDstMac()))){
                    doSet(allMap,DST_MAC_ADDRESS_WHITE,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrcMac()))){
                    doSet(allMap,SRC_MAC_ADDRESS_WHITE,str);
                }
                if (fiveDimensionFilter.getProtocolId() != 0){
                    String var = Common.PROTOCOL_STR_TO_INT.get(fiveDimensionFilter.getProtocolId());
                    System.out.print(" ");
                    /*
                     * 如果添加的协议ID，map中无法找到对应的协议，那么就加other
                     */
                    if (var == null){
                        doSet(allMap,PROTOCOL_WHITE,OTHER);
                    }else{
                        doSet(allMap,PROTOCOL_WHITE, var);
                    }
                }
            }else{
                //黑名单
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDstIp()))){
                    doSet(allMap,DST_IP_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrcIp()))){
                    doSet(allMap,SRC_IP_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDstPort()))){
                    doSet(allMap,DST_PORT_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrcPort()))){
                    doSet(allMap,SRC_PORT_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDstMac()))){
                    doSet(allMap,DST_MAC_ADDRESS_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrcMac()))){
                    doSet(allMap,SRC_MAC_ADDRESS_BLACK,str);
                }
                if (fiveDimensionFilter.getProtocolId() != 0){
                    String var = Common.PROTOCOL_STR_TO_INT.get(fiveDimensionFilter.getProtocolId());
                    /*
                     * 如果添加的协议ID，map中无法找到对应的协议，那么就加other
                     */
                    if (var == null){
                        doSet(allMap,PROTOCOL_BLACK,OTHER);
                    }else{
                        doSet(allMap,PROTOCOL_BLACK, var);
                    }
                }
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
                case DST_IP_BLACK:
                    dstIpBlackMap = allMap.get(DST_IP_BLACK);
                    break;
                case SRC_IP_BLACK:
                    srcIpBlackMap = allMap.get(SRC_IP_BLACK);
                    break;
                case DST_PORT_BLACK:
                    dstPortBlackMap = allMap.get(DST_PORT_BLACK);
                    break;
                case SRC_PORT_BLACK:
                    srcPortBlackMap = allMap.get(SRC_PORT_BLACK);
                    break;
                case PROTOCOL_BLACK:
                    protocolBlackMap = allMap.get(PROTOCOL_BLACK);
                    break;
                case SRC_MAC_ADDRESS_BLACK:
                    srcMacAddressBlack = allMap.get(SRC_MAC_ADDRESS_BLACK);
                    break;
                case DST_MAC_ADDRESS_BLACK:
                    dstMacAddressBlack = allMap.get(DST_MAC_ADDRESS_BLACK);
                    break;
                default:
                    log.error("code error in five dimension filter , not define {}", s);
                    break;
            }
        }
    }

    /**
     *
     * @param allMap
     * @param type 类型，如目的IP白名单
     * @param str 信息，如目的IP、目的MAC地址等
     */
    private void doSet(HashMap<String,HashMap<String,String>> allMap,String type , String str){
        allMap.putIfAbsent(type , new HashMap<>());
        allMap.get(type).put(str , str);
    }

    public BadPacket OK(FvDimensionLayer layer){
        if (protocolWhiteMap.containsKey(layer.frame_protocols[0])
            && srcPortWhiteMap.containsKey(layer.src_port[0])
            && dstPortWhiteMap.containsKey(layer.dst_port[0])
            && srcIpWhiteMap.containsKey(layer.ip_src[0])
            && dstIpWhiteMap.containsKey(layer.ip_dst[0])
            && dstMacAddressWhite.containsKey(layer.eth_dst[0])
            && srcMacAddressWhite.containsKey(layer.eth_src[0])) {
            return null;
        }
        else{
            //System.out.println(layer.frame_protocols[0]);
            if (layer.frame_protocols[0].equals("tcp"))
                return new BadPacket.Builder(FV_DIMENSION)
                        .set_five_Dimension(layer)
                        .setDangerLevel(DangerLevel.DANGER)
                        .setComment("五元组不存在于白名单")
                        .build();
            else
                return null;
        }
    }

    public BadPacket ERROR(FvDimensionLayer layer){
        BadPacket badPacketBuilder = null;
        if (protocolBlackMap.containsKey(layer.frame_protocols[0])){
            badPacketBuilder = new BadPacket();
            badPacketBuilder.setLayer(layer);
            badPacketBuilder.addComment("黑名单协议");
            badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            badPacketBuilder.setBadType(FV_DIMENSION);
            badPacketBuilder.setComment(badPacketBuilder.getComment());
            return badPacketBuilder;
        }
        if (srcPortBlackMap.containsKey(layer.src_port[0])){
            badPacketBuilder = new BadPacket();
            badPacketBuilder.setLayer(layer);
            badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            badPacketBuilder.addComment("黑名单源端口");
            badPacketBuilder.setBadType(FV_DIMENSION);
            badPacketBuilder.setComment(badPacketBuilder.getComment());
            return badPacketBuilder;
        }
        if (dstPortBlackMap.containsKey(layer.dst_port[0])){
            badPacketBuilder = new BadPacket();
            badPacketBuilder.setLayer(layer);
            badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            badPacketBuilder.addComment("黑名单目的端口");
            badPacketBuilder.setBadType(FV_DIMENSION);
            badPacketBuilder.setComment(badPacketBuilder.getComment());
            return badPacketBuilder;
        }
        if (srcIpBlackMap.containsKey(layer.ip_src[0])){
            badPacketBuilder = new BadPacket();
            badPacketBuilder.setLayer(layer);
            badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            badPacketBuilder.addComment("黑名单源IP");
            badPacketBuilder.setBadType(FV_DIMENSION);
            badPacketBuilder.setComment(badPacketBuilder.getComment());
            return badPacketBuilder;
        }
        if (dstIpBlackMap.containsKey(layer.ip_dst[0])){
            badPacketBuilder = new BadPacket();
            badPacketBuilder.setLayer(layer);
            badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            badPacketBuilder.addComment("黑名单目的IP");
            badPacketBuilder.setBadType(FV_DIMENSION);
            badPacketBuilder.setComment(badPacketBuilder.getComment());
            return badPacketBuilder;
        }
        if (dstMacAddressBlack.containsKey(layer.eth_dst[0])){
            badPacketBuilder = new BadPacket();
            badPacketBuilder.setLayer(layer);
            badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            badPacketBuilder.addComment("黑名单目的MAC");
            badPacketBuilder.setBadType(FV_DIMENSION);
            badPacketBuilder.setComment(badPacketBuilder.getComment());
            return badPacketBuilder;
        }
        if (srcMacAddressBlack.containsKey(layer.eth_src[0])){
            badPacketBuilder = new BadPacket();
            badPacketBuilder.setLayer(layer);
            badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            badPacketBuilder.addComment("黑名单源MAC");
            badPacketBuilder.setBadType(FV_DIMENSION);
            badPacketBuilder.setComment(badPacketBuilder.getComment());
            return badPacketBuilder;
        }
        return null;
    }

    @Override
    public String toString() {
        return "FiveDimensionPacketFilter{" +
                "srcIpWhiteMap=" + srcIpWhiteMap +
                ", srcIpBlackMap=" + srcIpBlackMap +
                ", srcPortWhiteMap=" + srcPortWhiteMap +
                ", srcPortBlackMap=" + srcPortBlackMap +
                ", protocolWhiteMap=" + protocolWhiteMap +
                ", protocolBlackMap=" + protocolBlackMap +
                ", dstIpWhiteMap=" + dstIpWhiteMap +
                ", dstIpBlackMap=" + dstIpBlackMap +
                ", dstPortWhiteMap=" + dstPortWhiteMap +
                ", dstPortBlackMap=" + dstPortBlackMap +
                ", filterName='" + filterName + '\'' +
                '}';
    }
}
