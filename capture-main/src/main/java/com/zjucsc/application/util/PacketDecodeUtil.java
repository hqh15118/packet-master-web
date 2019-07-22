package com.zjucsc.application.util;

import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.tshark.bean.CollectorState;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.S7CommPacket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public static int decodeFuncode(String protocol , String str_fun_code ){
        if (StringUtils.isBlank(str_fun_code)){
            return -1;
        }
        //System.out.println(str_fun_code);
        int fun_code = -1;
        try {
            fun_code = Integer.decode(str_fun_code);
        }catch (NumberFormatException e){
            log.error("exception when decode protocol {} fun_code {}" , protocol , str_fun_code , e);
        }
        return fun_code;
    }


    public static String discernPacket(FvDimensionLayer layer){
//        String protocolStack = layer.frame_protocols[0];
//        if (protocolStack.endsWith("s7comm")){
//            return "s7comm";
//        }
//        if (protocolStack.endsWith("modbus"))
//        {
//            return MODBUS;
//        }
//        else if(protocolStack.endsWith("dnp3"))
//        {
//
//        }
//        else if(protocolStack.endsWith("104apci"))
//        {
//            return IEC104_APCI;
//        }
//        else if (protocolStack.endsWith("104asdu")){
//            return IEC104_ASDU;
//        }
        if(layer.frame_protocols[0].endsWith("data"))
        {
            return UDP;
        }
//        else{
        return getUnDefinedPacketProtocol(layer.frame_protocols[0]);
//        }
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

}