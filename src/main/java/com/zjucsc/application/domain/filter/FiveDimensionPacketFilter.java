package com.zjucsc.application.domain.filter;

import com.zjucsc.application.config.BadPacketDangerLevel;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.BadPacket;
import com.zjucsc.application.domain.entity.FVDimensionFilterEntity;
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


    private String filterName;
    public FiveDimensionPacketFilter(String filterName){
        this.filterName = filterName;
    }

    public void setFilterList(List<FVDimensionFilterEntity.FiveDimensionFilter> filterList){
        HashMap<String,HashMap<String, String>> allMap = new HashMap<>();
        for (FVDimensionFilterEntity.FiveDimensionFilter fiveDimensionFilter : filterList) {
            String str = null;
            if (fiveDimensionFilter.getFilterType() == 0){
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
                if (fiveDimensionFilter.getProtocolId()!=0){
                    String var = Common.PROTOCOL_STR_TO_INT.get(fiveDimensionFilter.getProtocolId());
                    System.out.println(" ");//这个啥也没做，就是防止IDE自动重构
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
                if (fiveDimensionFilter.getProtocolId()!=0){
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
                case "dst_ip_white":
                    dstIpWhiteMap = allMap.get("dst_ip_white");
                    break;
                case "src_ip_white":
                    srcIpWhiteMap = allMap.get("src_ip_white");
                    break;
                case "dst_port_white":
                    dstPortWhiteMap = allMap.get("dst_port_white");
                    break;
                case "src_port_white":
                    srcPortWhiteMap = allMap.get("src_port_white");
                    break;
                case "protocol_white":
                    protocolWhiteMap = allMap.get("protocol_white");
                    break;
                case "dst_ip_black":
                    dstIpBlackMap = allMap.get("dst_ip_black");
                    break;
                case "src_ip_black":
                    srcIpBlackMap = allMap.get("src_ip_black");
                    break;
                case "dst_port_black":
                    dstPortBlackMap = allMap.get("dst_port_black");
                    break;
                case "src_port_black":
                    srcPortBlackMap = allMap.get("src_port_black");
                    break;
                case "protocol_black":
                    protocolBlackMap = allMap.get("protocol_black");
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
//                    .setDangerLevel(BadPacketDangerLevel.DANGER)
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
            badPacketBuilder.setDangerLevel(BadPacketDangerLevel.VERY_DANGER);
        }
        if (srcPortBlackMap.containsKey(wrapper.fiveDimensionPacket.src_port)){
            if (badPacketBuilder == null){
                badPacketBuilder = new BadPacket();
                badPacketBuilder.setFiveDimensionPacket(wrapper.fiveDimensionPacket);
                badPacketBuilder.setDangerLevel(BadPacketDangerLevel.VERY_DANGER);
            }
            badPacketBuilder.addComment("黑名单源端口|");
        }
        if (dstPortBlackMap.containsKey(wrapper.fiveDimensionPacket.dis_port)){
            if (badPacketBuilder == null){
                badPacketBuilder = new BadPacket();
                badPacketBuilder.setFiveDimensionPacket(wrapper.fiveDimensionPacket);
                badPacketBuilder.setDangerLevel(BadPacketDangerLevel.VERY_DANGER);
            }
            badPacketBuilder.addComment("黑名单目的端口|");
        }
        if (srcIpBlackMap.containsKey(wrapper.fiveDimensionPacket.src_ip)){
            if (badPacketBuilder == null){
                badPacketBuilder = new BadPacket();
                badPacketBuilder.setFiveDimensionPacket(wrapper.fiveDimensionPacket);
                badPacketBuilder.setDangerLevel(BadPacketDangerLevel.VERY_DANGER);
            }
            badPacketBuilder.addComment("黑名单源IP|");
        }
        if (dstIpBlackMap.containsKey(wrapper.fiveDimensionPacket.dis_ip)){
            if (badPacketBuilder == null){
                badPacketBuilder = new BadPacket();
                badPacketBuilder.setFiveDimensionPacket(wrapper.fiveDimensionPacket);
                badPacketBuilder.setDangerLevel(BadPacketDangerLevel.VERY_DANGER);
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
