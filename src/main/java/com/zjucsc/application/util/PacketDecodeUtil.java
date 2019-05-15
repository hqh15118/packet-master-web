package com.zjucsc.application.util;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.tshark.domain.packet.S7CommPacket;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.zjucsc.application.config.PACKET_PROTOCOL.*;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 21:19
 */
@Slf4j
public class PacketDecodeUtil {

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
//        data[byteArraySize] = (byte) ((Character.digit(s.charAt(len - 2), 16) << 4)
//                + Character.digit(s.charAt(len - 1), 16));
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

    //  0000 0000/0000 0000/0000 1001/0011 1101/0010 0011/0111 0110/1010 0000/0000 0000
    public static String decodeTimeStamp(byte[] payload , int offset){
        if (payload.length == 0){
            return simpleDateFormatThreadLocal.get().format(new Date());
        }
        if (payload.length < offset ) {
            return simpleDateFormatThreadLocal.get().format(new Date());
        }
        StringBuilder sb = stringBuilderThreadLocal.get();
        sb.delete(0,sb.length());
        int len = payload.length;
        offset = len - offset;          //offset 就是start index
//        for (int i = offset; i < payload.length; i++) {
//            System.out.println( Integer.toHexString(Byte.toUnsignedInt(payload[i])));
//        }
        int year = (Byte.toUnsignedInt(payload[offset]) << 3) + ((payload[offset + 1] & 0b11100000) >>> 5);
        int month = ((payload[offset + 1] & 0b00011110) >> 1);
        int day = ((payload[offset + 1] & 1) << 4) + ((payload[offset + 2] & 0b11110000) >>> 4);
        int hour = ((Byte.toUnsignedInt(payload[offset + 2]) & 0b00001111) << 1) + ((payload[offset + 3] & 0b10000000) >>> 7);
        int minute = ((payload[offset + 3] & 0b01111110) >> 1);
        int second = ((Byte.toUnsignedInt(payload[offset + 3]) & 1) << 5)
                + ((payload[offset + 4] & 0b11111000) >>> 3);
        int millSecond = ((Byte.toUnsignedInt(payload[offset + 4]) & 0b00000111) << 7) +
                ((payload[offset + 5] & 0b11111110) >>> 1);
        int uSecond = ((Byte.toUnsignedInt(payload[offset + 5]) & 1) << 9) + (Byte.toUnsignedInt(payload[offset + 6]) << 1)
                + ((payload[offset + 7] & 0b10000000) >>> 7);
        int naoSecond = ((payload[offset + 7] & 0b01110000) >> 4) * 200;

        return sb.append(year).append(" 年 ")
                .append(month).append(" 月 ")
                .append(day).append(" 天 ")
                .append(hour).append(" 时 ")
                .append(minute).append(" 分 ")
                .append(second).append(" 秒 ")
                .append(millSecond).append( " 毫秒 ")
                .append(uSecond).append(" 微秒 ")
                .append(naoSecond).append(" 纳秒 ").toString();
    }


    /**
     * 不同的协议的tshark得到的功能码的格式不一样，要全部解成int
     * @param str_fun_code 字符串形式的功能码
     * @param protocol 协议
     * @return 功能码int
     */
    public static int decodeFuncode(String protocol , String str_fun_code ){
        int fun_code = -1;
        try {
            switch (protocol) {
                case MODBUS:
                    //fun_code = Integer.decode(str_fun_code);
                    //break;
                case S7:
                case S7_Ack_data:
                case S7_JOB:
                case IEC104:
                    fun_code = Integer.decode(str_fun_code);
                    break;
            }
        }catch (NumberFormatException e){
            log.error("exception when decode protocol {} fun_code {}" , protocol , str_fun_code , e);
        }
        return fun_code;
    }


    /**
     * 用于识别报文协议
     * @param protocolStack 协议栈
     * @param otherInfo 用于识别具体协议的其他信息
     * @return 系统格式的协议信息
     * @see com.zjucsc.application.config.PACKET_PROTOCOL
     */
    public static String discernPacket(String protocolStack ,  Object...otherInfo){
        if (protocolStack.endsWith("tcp")){
            return TCP;
        }
        else if (protocolStack.endsWith("modbus"))
        {
            return MODBUS;
        }else if(protocolStack.endsWith("s7comm"))
        {
            String rosctr = ((String) otherInfo[0]);
            if (S7CommPacket.ACK_DATA.equals(rosctr)){
                return S7_Ack_data;
            }else if (S7CommPacket.JOB.equals(rosctr)) {
                return S7_JOB;
            }else{
                return S7;
            }
        }
        else if(protocolStack.endsWith("dnp3"))
        {
            return DNP3_0;
        }
        else if(protocolStack.endsWith("iec60870_104"))
        {
            return IEC104;
        }
        else if(protocolStack.endsWith("data"))
        {//udp...fixme
            return UDP;
        }
        else{
            return protocolStack;
        }
    }

    /**
     * 用于识别<设备采集器>状态
     * @param payload 负载 ， 最后24个字节是自己加上去的需要解析的部分
     * @return 采集器状态，返回null表示状态没有发生改变，返回instance，表示状态发生改变
     *
     * **********************************
     * [2byte]    [1byte]     [1byte]       [20字节]
     *  设备ID     A口状态      B口状态         其他
     * *********************************/
    public static CollectorState decodeCollectorState(byte[] payload , int offset){
        if (payload.length==0){
            return null;
        }
        if (offset == 24){
            return null;
        }
        int start = payload.length - offset;//payload中自定义的字节数组的开始位置
        int collectorId = ByteUtils.bytesToShort(payload,start,2);
        CollectorState state = null;
        int A_state = Byte.toUnsignedInt(payload[start + 2]);
        int B_state = Byte.toUnsignedInt(payload[start + 3]);
        if ((state = Common.COLLECTOR_STATE_MAP.get(collectorId))==null){
            //还没有定义过该设备，初始化该设备状态
            Common.COLLECTOR_STATE_MAP.put(collectorId , new CollectorState(collectorId , -1,-1,
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
            Common.COLLECTOR_STATE_MAP.put(collectorId , newState);
            return newState;
        }
        return null;
    }


    public static int decodeCollectorId(byte[] payload , int offset){
        if (payload.length < offset){
            return -1;
        }
        int start = payload.length - offset;
        return payload[start] << 8 + payload[start + 1];
    }

    public static long decodeCollectorDelay(byte[] payload , int offsetFromEnd) {
        //
        int start = payload.length - offsetFromEnd;     //24 - 4
        return ByteUtils.bytesToLong(payload,start,4);
    }
}
