package com.zjucsc.application.domain.filter;

import com.zjucsc.application.config.DangerLevel;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.BadPacket;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
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
public class FiveDimensionPacketFilter {

    public static final String DST_IP_WHITE = "dst_ip_white";
    public static final String SRC_IP_WHITE = "src_ip_white";
    public static final String DST_PORT_WHITE = "dst_port_white";
    public static final String SRC_PORT_WHITE = "src_port_white";
    public static final String PROTOCOL_WHITE = "protocol_white";
    public static final String DST_IP_BLACK = "dst_ip_black";
    public static final String SRC_IP_BLACK = "src_ip_black";
    public static final String DST_PORT_BLACK = "dst_port_black";
    public static final String SRC_PORT_BLACK = "src_port_black";
    public static final String PROTOCOL_BLACK = "protocol_black";
    public static final String SRC_MAC_ADDRESS_WHITE = "src_mac_address_white";
    public static final String SRC_MAC_ADDRESS_BLACK = "src_mac_address_black";
    public static final String DST_MAC_ADDRESS_WHITE = "dst_mac_address_white";
    public static final String DST_MAC_ADDRESS_BLACK = "dst_mac_address_black";

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

    public void setFilterList(List<FvDimensionFilter> filterList){
        this.filterList = filterList;
        HashMap<String,HashMap<String, String>> allMap = new HashMap<>();
        for (FvDimensionFilter fiveDimensionFilter : filterList) {
            String str = null;
            if (fiveDimensionFilter.getFilter_type() == 0){
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDst_ip()))){
                    doSet(allMap,DST_IP_WHITE,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrc_ip()))){
                    doSet(allMap,SRC_IP_WHITE,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDst_port()))){
                    doSet(allMap,DST_PORT_WHITE,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrc_port()))){
                    doSet(allMap,SRC_PORT_WHITE,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDst_mac()))){
                    doSet(allMap,DST_MAC_ADDRESS_WHITE,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrc_mac()))){
                    doSet(allMap,SRC_MAC_ADDRESS_WHITE,str);
                }
                if (fiveDimensionFilter.getProtocol_id() != 0){
                    String var = Common.PROTOCOL_STR_TO_INT.get(fiveDimensionFilter.getProtocol_id());
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
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDst_ip()))){
                    doSet(allMap,DST_IP_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrc_ip()))){
                    doSet(allMap,SRC_IP_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDst_port()))){
                    doSet(allMap,DST_PORT_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrc_port()))){
                    doSet(allMap,SRC_PORT_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getDst_mac()))){
                    doSet(allMap,DST_MAC_ADDRESS_BLACK,str);
                }
                if (StringUtils.isNotBlank((str = fiveDimensionFilter.getSrc_mac()))){
                    doSet(allMap,SRC_MAC_ADDRESS_BLACK,str);
                }
                if (fiveDimensionFilter.getProtocol_id() != 0){
                    String var = Common.PROTOCOL_STR_TO_INT.get(fiveDimensionFilter.getProtocol_id());
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

    private void doSet(HashMap<String,HashMap<String,String>> allMap,String type , String str){
        allMap.computeIfAbsent(type, k -> new HashMap<String, String>());
        allMap.get(type).put(str , str);
    }

    public HashMap<String, String> getSrcIpWhiteMap() {
        return srcIpWhiteMap;
    }

    public HashMap<String, String> getSrcIpBlackMap() {
        return srcIpBlackMap;
    }

    public HashMap<String, String> getSrcPortWhiteMap() {
        return srcPortWhiteMap;
    }

    public HashMap<String, String> getSrcPortBlackMap() {
        return srcPortBlackMap;
    }

    public HashMap<String, String> getProtocolWhiteMap() {
        return protocolWhiteMap;
    }

    public HashMap<String, String> getProtocolBlackMap() {
        return protocolBlackMap;
    }

    public HashMap<String, String> getDstIpWhiteMap() {
        return dstIpWhiteMap;
    }

    public HashMap<String, String> getDstIpBlackMap() {
        return dstIpBlackMap;
    }

    public HashMap<String, String> getDstPortWhiteMap() {
        return dstPortWhiteMap;
    }

    public HashMap<String, String> getDstPortBlackMap() {
        return dstPortBlackMap;
    }

    public HashMap<String, String> getSrcMacAddressWhite() {
        return srcMacAddressWhite;
    }

    public HashMap<String, String> getSrcMacAddressBlack() {
        return srcMacAddressBlack;
    }

    public HashMap<String, String> getDstMacAddressWhite() {
        return dstMacAddressWhite;
    }

    public HashMap<String, String> getDstMacAddressBlack() {
        return dstMacAddressBlack;
    }

    public BadPacket OK(FiveDimensionPacketWrapper wrapper){
        if (protocolWhiteMap.containsKey(wrapper.fiveDimensionPacket.protocol)){
            return null;
        }else if (srcPortWhiteMap.containsKey(wrapper.fiveDimensionPacket.src_port)){
            return null;
        }else if (dstPortWhiteMap.containsKey(wrapper.fiveDimensionPacket.dis_port)){
            return null;
        }else if (srcIpWhiteMap.containsKey(wrapper.fiveDimensionPacket.src_ip)){
            return null;
        }else if (dstIpWhiteMap.containsKey(wrapper.fiveDimensionPacket.dis_ip)){
            return null;
        }else{
//            return new BadPacket.Builder(FV_DIMENSION)
//                    .set_five_Dimension(wrapper.fiveDimensionPacket)
//                    .setDangerLevel(DangerLevel.DANGER)
//                    .setComment("白名单未匹配五元组")
//                    .build();
            return null;
        }
    }

    public BadPacket ERROR(FiveDimensionPacketWrapper wrapper){
        BadPacket badPacketBuilder = null;
        if (protocolBlackMap.containsKey(wrapper.fiveDimensionPacket.protocol)){
            badPacketBuilder = new BadPacket();
            badPacketBuilder.setFiveDimensionPacket(wrapper.fiveDimensionPacket);
            badPacketBuilder.addComment("黑名单协议|");
            badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
        }
        if (srcPortBlackMap.containsKey(wrapper.fiveDimensionPacket.src_port)){
            if (badPacketBuilder == null){
                badPacketBuilder = new BadPacket();
                badPacketBuilder.setFiveDimensionPacket(wrapper.fiveDimensionPacket);
                badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            }
            badPacketBuilder.addComment("黑名单源端口|");
        }
        if (dstPortBlackMap.containsKey(wrapper.fiveDimensionPacket.dis_port)){
            if (badPacketBuilder == null){
                badPacketBuilder = new BadPacket();
                badPacketBuilder.setFiveDimensionPacket(wrapper.fiveDimensionPacket);
                badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            }
            badPacketBuilder.addComment("黑名单目的端口|");
        }
        if (srcIpBlackMap.containsKey(wrapper.fiveDimensionPacket.src_ip)){
            if (badPacketBuilder == null){
                badPacketBuilder = new BadPacket();
                badPacketBuilder.setFiveDimensionPacket(wrapper.fiveDimensionPacket);
                badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            }
            badPacketBuilder.addComment("黑名单源IP|");
        }
        if (dstIpBlackMap.containsKey(wrapper.fiveDimensionPacket.dis_ip)){
            if (badPacketBuilder == null){
                badPacketBuilder = new BadPacket();
                badPacketBuilder.setFiveDimensionPacket(wrapper.fiveDimensionPacket);
                badPacketBuilder.setDangerLevel(DangerLevel.VERY_DANGER);
            }
            badPacketBuilder.addComment("黑名单目的IP|");
        }
        if (badPacketBuilder!=null){
            badPacketBuilder.setBadType(FV_DIMENSION);
            badPacketBuilder.setComment(badPacketBuilder.getComment());
        }
        return badPacketBuilder;
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
