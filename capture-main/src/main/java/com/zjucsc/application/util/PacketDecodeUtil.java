package com.zjucsc.application.util;

import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.common.util.ByteUtil;
import com.zjucsc.common.util.CommonUtil;
import com.zjucsc.tshark.bean.CollectorState;
import com.zjucsc.tshark.packets.Dnp3_0Packet;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.IEC104Packet;
import com.zjucsc.tshark.packets.S7CommPacket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import static com.zjucsc.application.config.PACKET_PROTOCOL.*;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 21:19
 */
@Slf4j
public class PacketDecodeUtil {


    /**
     * cache3
     */
    private final static ConcurrentHashMap<Integer, CollectorState> COLLECTOR_STATE_MAP = new ConcurrentHashMap<>();
    public static Map<Integer,CollectorState> getCollectorStateMap(){
        return COLLECTOR_STATE_MAP;
    }
    /*
     *  string : 03:00:00:1f:02:f0:80:32:01:00:00:cc:c1:00:0e:00:00:04:01:12:0a:10:02:00:11:00:01:84:00:00:20
     *  ----> byte[] : 03 00 00 1f 02 f0 80 32 01 00 00 cc c1 00 0e 00 00 04 01 12 0a 10 02 00 11 00 01 84 00 00 20
     */
    private static final byte[] EMPTY = new byte[]{};
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        if (len == 0){
            return EMPTY;
        }
        int byteArraySize = len / 3;
        byte[] data = new byte[byteArraySize + 1];
        for (int i = 0 ; i < len; i+=3) {
            data[i / 3] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        data[byteArraySize ] = (byte) ((Character.digit(s.charAt(len - 2), 16) << 4)
                + Character.digit(s.charAt(len - 1), 16));
        return data;
    }

    /**
     * decode non : string
     * String trailer = "00020d04fc6aa8defba27a10fc6aa8defba27a80";
     *         String fsc = "0x00000075";
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray2(String s) {
        return hexStringToByteArray2(s, 0 );
    }


    public static byte[] hexStringToByteArray2(String s , int offset) {
        int len = s.length();
        if (len == 0){
            return EMPTY;
        }
        int byteArraySize = (len - offset) >>> 1;
        byte[] data = new byte[byteArraySize];
        int j = 0;
        for (int i = offset ; i < len; i += 2) {
            data[j] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
            j++;
        }
        return data;
    }

    private static ThreadLocal<StringBuilder> stringBuilderThreadLocal
            = ThreadLocal.withInitial(() -> new StringBuilder(100));
    /**
     *
     * @param payload tcp payload
     * @param offset offset from payload end
     * @return
     */


    private static ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss:SSS");
        }
    };

    private static int offset1 = Byte.toUnsignedInt((byte)0b11100000);
    private static int offset2 = Byte.toUnsignedInt((byte)0b10000000);
    private static int offset3 = Byte.toUnsignedInt((byte)0b11111000);
    private static int offset4 = Byte.toUnsignedInt((byte)0b11111110);
    private static int offset5 = Byte.toUnsignedInt((byte)0b11110000);

    /**
     *
     * @param payload 负载
     * @param offset 从后往前的offset个字节为start index
     * @return
     */
    public static String decodeTimeStamp(byte[] payload , int offset, FvDimensionLayer layer){
        if (payload.length < offset){
            return simpleDateFormatThreadLocal.get().format(new Date());
        }
        StringBuilder sb = stringBuilderThreadLocal.get();
        sb.delete(0,sb.length());
        int len = payload.length;
        offset = len - offset;          //offset 就是start index
        int year = (Byte.toUnsignedInt(payload[offset]) << 3) + ((payload[offset + 1] & offset1) >>> 5);
        int month = ((payload[offset + 1] & 0b00011110) >> 1);
        int day = ((payload[offset + 1] & 1) << 4) + ((payload[offset + 2] & offset5) >>> 4);
        int hour = ((payload[offset + 2] & 0b00001111) << 1) + ((payload[offset + 3] & offset2) >>> 7);
        int minute = ((payload[offset + 3] & 0b01111110) >> 1);
        int second = ((payload[offset + 3] & 1) << 5)
                + ((payload[offset + 4] & offset3) >>> 3);
        int millSecond = ((payload[offset + 4] & 0b00000111) << 7) +
                ((payload[offset + 5] & offset4) >>> 1);
        int uSecond = ((payload[offset + 5] & 1) << 9) + (Byte.toUnsignedInt(payload[offset + 6]) << 1)
                + ((payload[offset + 7] & offset2) >>> 7);
        int naoSecond = ((payload[offset + 7] & 0b01110000) >> 4) * 200;
        layer.timeStampInLong = minute * 60 * 1000 + second * 1000 + millSecond;
        return sb.append(year).append("-")
                .append(month).append("-")
                .append(day).append(" ")
                .append(hour).append(":")
                .append(minute).append(":")
                .append(second).append(":")
                .append(millSecond).append( ":")
                .append(uSecond).append(":")
                .append(naoSecond).toString();
    }


    /**
     * 不同的协议的tshark得到的功能码的格式不一样，要全部解成int
     * @param str_fun_code 字符串形式的功能码
     * @param protocol 协议
     * @return 功能码int
     */
    public static String decodeFuncode(String protocol , String str_fun_code ){
        if (StringUtils.isBlank(str_fun_code)){
            return "--";
        }
        //System.out.println(str_fun_code);
        int fun_code = -1;
        try {
            fun_code = Integer.decode(str_fun_code);
        }catch (NumberFormatException e){
            log.error("exception when decode protocol {} fun_code {}" , protocol , str_fun_code , e);
        }
        return String.valueOf(fun_code);
    }

    //eth:llc:tcp:data
    public static String discernPacket(String protocolStack){
        if(protocolStack.endsWith("data"))
        {
            StringBuilder sb = CommonUtil.getGlobalStringBuilder();
            char ch;
            for (int i = protocolStack.length() - 6;; i--) {
                if ((ch = protocolStack.charAt(i))!=':'){
                    sb.append(ch);
                }else{
                    break;
                }
            }
            return sb.reverse().toString();
        }
        return getUnDefinedPacketProtocol(protocolStack);
    }

    public static String decodeS7Protocol(FvDimensionLayer layer){
            S7CommPacket.LayersBean layersX = ((S7CommPacket.LayersBean) layer);
            String rosctr = layersX.s7comm_header_rosctr[0];
            if (S7CommPacket.ACK_DATA.equals(rosctr)){
                return S7;
            }else if (S7CommPacket.JOB.equals(rosctr)) {
                return S7;
            }else if (S7CommPacket.USER_DATA.equals(rosctr)){
                return S7_User_data;
            }
            else{
                return S7;
            }
    }

    public static String decodeS7Protocol2(FvDimensionLayer layer){
        if (layer.funCodeMeaning.equals("循环数据") || layer.funCodeMeaning.equals("CPU功能")){
            return S7_User_data;
        }else{
            return S7;
        }
    }
    public static String getUnDefinedPacketProtocol(String protocolStack){
        StringBuilder sb = stringBuilderThreadLocal.get();
        sb.delete(0,sb.length());
        int index = protocolStack.lastIndexOf(":");
        if (index < 0){
            return protocolStack;
        }
        for (int i = index + 1 ; i < protocolStack.length() ; i ++){
            sb.append(protocolStack.charAt(i));
        }
        return sb.toString();
    }

    private static final int STATE_REF = 0b00000111;
    /**
     * 用于识别<设备采集器>状态
     * @param payload 负载 ， 最后24个字节是自己加上去的需要解析的部分
     * @return 采集器状态，返回null表示状态没有发生改变，返回instance，表示状态发生改变
     *
     * **********************************
     * [2byte]    [1byte]     [1byte]       [20字节]
     *  设备ID     A口状态      B口状态         其他
     * *********************************/
    public static CollectorState decodeCollectorState(byte[] payload , int offset , int collectorId){
        if (payload.length < 24){
            return null;
        }

        int start = payload.length - offset;//payload中自定义的字节数组的开始位置
        CollectorState state;
        int A_state = payload[start + 2] & STATE_REF;
        int B_state = payload[start + 3] & STATE_REF;
        if ((state = COLLECTOR_STATE_MAP.get(collectorId))==null){
            //还没有定义过该设备，初始化该设备状态
            COLLECTOR_STATE_MAP.put(collectorId , new CollectorState(collectorId , -1,-1,
                    A_state,B_state));
            return null;
        }
        //当前检测到的A或者B口的状态与之前检测到的不同，说明A或者B或者A和B口状态发生了改变
        //前端只需要比较lastState和currentState是否出现偏差，即可知道哪个或者全部口是否发生了状态改变
        if (A_state != state.getA_currentState() || B_state != state.getB_currentState()){
            CollectorState newState = new CollectorState(collectorId ,
                    state.getA_currentState(),
                    state.getB_currentState(),
                    A_state,B_state);
            COLLECTOR_STATE_MAP.put(collectorId , newState);
            return newState;
        }
        return null;
    }


    public static int decodeCollectorId(byte[] payload , int offset){
        if (payload.length < offset){
            return -1;
        }
        int start = payload.length - offset;
        return (payload[start] << 8) + payload[start + 1];
    }

    public static int decodeCollectorDelay(byte[] payload , int offsetFromEnd) {
        //
        int start = payload.length - offsetFromEnd;     //24 - 4
        return ByteUtil.bytesToInt(payload,start);
    }


    private static final int trailerLength = 59;
    private static final int fscLength = 10;
    private static final int trailerAndFscLength = 24;

    public static byte[] decodeTrailerAndFsc(String trailerAndFsc){
        if (trailerAndFsc.length() == 0){
            return EMPTY;
        }
        if (!trailerAndFsc.contains(":")){  //
            return hexStringToByteArray2(trailerAndFsc);
        }
        int trailerLength = PacketDecodeUtil.trailerLength;
        int fscLength = PacketDecodeUtil.fscLength;
        byte[] allBytes = new byte[PacketDecodeUtil.trailerAndFscLength];
        int i = 0;
        for (; i < trailerLength; i += 3) {
            allBytes[i / 3] = (byte) ((Character.digit(trailerAndFsc.charAt(i), 16) << 4)
                    + Character.digit(trailerAndFsc.charAt(i+1), 16));
        }
        i = i / 3;
        int point = 0;
        for (int j = trailerLength + 2 , len = trailerLength + fscLength; j < len; j+=2) {
            allBytes[i + point] = (byte) ((Character.digit(trailerAndFsc.charAt(j), 16) << 4)
                    + Character.digit(trailerAndFsc.charAt(j+1), 16));
            point++;
        }
        return allBytes;
    }


    public static String getAttackBeanInfo(FvDimensionLayer layer)
    {
        String protocol = getPacketDetailProtocol(layer);
        String funCodeStr = layer.funCode;
        if(protocol==null || funCodeStr.equals("--"))
        {
            return null;
        }
        int funCode;
        switch (protocol)
        {
            case "s7comm": {
                funCode = Integer.decode(funCodeStr);
                switch (funCode) {
                    case 0x04:
                        return "数据篡改攻击";
                    case 0x05:
                        return "非法读取数据";
                    case 0x1a:
                    case 0x1b:
                        return "代码篡改攻击";
                    case 0x1c:
                        return "代码异常攻击";
                    case 0x1d:
                    case 0x1e:
                    case 0x1f:
                        return "非法获取控制代码!";
                    case 0x28:
                    case 0x29:
                        return "配置篡改攻击";
                    default:
                        return "非法功能码";
                }
            }
            case "s7comm_user_data":
            {
                funCode = Integer.decode(funCodeStr);
                switch (funCode) {
                    case 0xf:
                        return "代码篡改攻击";
                    case 0x1:
                    case 0x0:
                        return "配置篡改攻击";
                    case 0x4:
                        return "非法访问CPU";
                    case 0x2:
                        return "非法读取数据";
                    default:
                        return "非法功能码";
                }
            }
            case "modbus":
            {
                funCode = Integer.decode(funCodeStr);
                switch (funCode) {
                    case 43:
                        return "嗅探攻击";
                    case 5:
                    case 15:
                    case 6:
                    case 16:
                    case 23:
                        return "数据篡改攻击";
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        return "非法读取数据";
                    case 22:
                        return "配置篡改攻击";
                    case 20:
                    case 21:
                        return "非法访问文件记录";
                    default:
                        return "非法功能码";
                }
            }
            case "opcua":
            {
                funCode = Integer.decode(funCodeStr);
                switch (funCode)
                {
                    case 527:
                    case 530:
                    case 533:
                    case 536:
                        return "嗅探攻击";
                    case 793:
                    case 796:
                        return "配置篡改攻击";
                    case 673:
                    case 676:
                        return "数据篡改攻击";
                    default:
                        return "非法功能码";
                }
            }
//            case "decrpc":
//            {
//                switch (funCodeStr)
//                {
//                    case "IOPCServerList":
//                    case "IOPCServerList2":
//                    case "IOPCBrowseServerAddressSpace":
//                        return "嗅探攻击";
//                    case "IOPCItemDeadbandMgt":
//                    case "IOPCItemSamplingMgt":
//                        return "配置篡改攻击";
//                    case "IOPCSyncIO":
//                    case "IOPCAsyncIO":
//                    case "IOPCAsyncIO2":
//                    case "IOPCSyncIO2":
//                    case "IOPCAsyncIO3":
//                        return "数据篡改攻击";
//                    default:
//                        return "非法功能码";
//                }
//                break;
//            }
            case "dnp3" :
                funCode = Integer.decode(funCodeStr);
                switch (getDnp3DetailType(layer)){
                    case "dnp3.0_pri":
                    {
                        switch (funCode) {
                            case 3:
                            case 4:
                                return "数据篡改攻击";
                            case 9:
                                return "嗅探攻击";
                            default:
                                return "非法功能码";
                        }
                    }
                    case "dnp3.0_set":
                    {
                        return "非法功能码";
                    }
                }
                break;
            case "mms":
            {
                funCode = Integer.decode(funCodeStr);
                switch (funCode)
                {
                    case 3:
                    case 7:
                    case 8:
                    case 10:
                    case 11:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 21:
                    case 22:
                    case 26:
                    case 28:
                    case 29:
                    case 31:
                    case 36:
                    case 38:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 47:
                    case 48:
                    case 51:
                    case 53:
                    case 54:
                    case 57:
                    case 58:
                    case 59:
                    case 69:
                    case 70:
                    case 75:
                    case 76:
                        return "配置篡改攻击";
                    case 6:
                    case 9:
                    case 12:
                    case 37:
                    case 45:
                    case 46:
                    case 49:
                    case 55:
                    case 61:
                    case 64:
                    case 71:
                        return "嗅探攻击";
                    default:
                        return "非法功能码";
                }
            }
            case "104asdu": {
                funCode = Integer.decode(funCodeStr);
                switch (funCode)
                {
                    case 100:
                        return "嗅探攻击";
                    case 45:
                    case 46:
                        return "数据篡改攻击";
                    case 48:
                    case 50:
                        return "配置篡改攻击";
                    default:
                        return "非法功能码";
                }
            }
            case "104apci":
            {
                //TODO
                return "非法功能码";
            }
            case "opcda" :
                switch (funCodeStr)
                {
                    case "IOPCBrowseServerAddressSpace":
                    case "IOPCServerList":
                    case "IOPCServerList2":
                    case "IOPCServer":
                        return "嗅探攻击";
                    case "IOPCSyncIO":
                    case "IOPCAsyncIO":
                    case "IOPCAsyncIO2":
                    case "IOPCSyncIO2":
                    case "IOPCAsyncIO3":
                        return "数据篡改攻击";
                    case "IOPCItemDeadbandMgt":
                    case "IOPCItemSamplingMgt":
                        return "配置篡改攻击";
                    default:
                        return "非法功能码";
                }
            default:
                return "非法协议";
        }
        return "非法协议";
    }

    /**
     * 判断报文的真实类型
     * s7comm、iec104、dnp
     * @param layer
     * @return
     */
    public static String getPacketDetailProtocol(FvDimensionLayer layer){
        switch (layer.protocol){
            case "s7comm" : return getS7commDetailType(layer);
            case "dnp3" : return getDnp3DetailType(layer);
        }
        return null;
    }

    private static String getDnp3DetailType(FvDimensionLayer layer){
        Dnp3_0Packet.LayersBean dnpPacket = ((Dnp3_0Packet.LayersBean) layer);
        int type = Integer.decode(dnpPacket.dnp3_ctl_prm[0]);
        switch (type){
            case 1: return PACKET_PROTOCOL.DNP3_0_PRI ;
            case 0: return PACKET_PROTOCOL.DNP3_0_SET ;
        }
        return "dnp3";
    }

    public static String getIEC104DetailType(FvDimensionLayer layer){
        IEC104Packet.LayersBean iec104_packet = ((IEC104Packet.LayersBean) layer);
        int type = Integer.decode(iec104_packet.iec104_type[0]);
        switch (type){
            case 0 :
                return PACKET_PROTOCOL.IEC104_ASDU;
            default:
                return PACKET_PROTOCOL.IEC104_APCI;
        }
    }

    public static String getS7commDetailType(FvDimensionLayer layer){
        S7CommPacket.LayersBean s7comm_packet = ((S7CommPacket.LayersBean) layer);
            //setFuncodeMeaning of s7comm
            if (s7comm_packet.s7comm_header_rosctr[0].equals(S7CommPacket.USER_DATA)) {
                //user data
                return PACKET_PROTOCOL.S7_User_data;
            } else {
                return PACKET_PROTOCOL.S7;
            }
    }

    public static byte[] getTcpPayload(byte[] rawData){
        /**
         * 前14个字节是以太网的头 [0,13]
         * (第15个字节的后四位 * 4)代表IP层头的长度 data[14] ==> ip_len [14,13+ip_len]
         * 【TCP】
         * 前12个字节都不是 [14+ip_len,25+ip_len]
         * 第13个字节的前4为代表TCP头的长度 data[26+ip_len] ==> tcp_len
         * 13+ip_len+tcp_len
         * @param rawData
         * @return
         */
        int ipHeaderLen = (rawData[14] & 0x0F) << 2;
        int tcpHeaderLen = ( rawData[26 + ipHeaderLen] & 0xF0) >> 2;
        int tcpPayloadStartIndex = 13 + ipHeaderLen + tcpHeaderLen + 1;
        int tcpPayloadLen = rawData.length - tcpPayloadStartIndex;
        byte[] tcpPayload = new byte[tcpPayloadLen];
        System.arraycopy(rawData,tcpPayloadStartIndex,tcpPayload,0,tcpPayload.length);
        return tcpPayload;
    }
}
