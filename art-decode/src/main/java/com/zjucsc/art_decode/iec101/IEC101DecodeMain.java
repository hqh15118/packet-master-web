package com.zjucsc.art_decode.iec101;

import com.zjucsc.art_decode.artconfig.IEC101Config;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.HashMap;
import java.util.Map;

public class IEC101DecodeMain {
    /*
    private static final int REMOTE_CONTROL = 1;    //监视方向的过程信息
    private static final int WATCH_PROGRESS_INFO = 2;   //遥控
    private static final int ARG_SETTING = 3;   //参数设置
    private static final int SYS_END_INIT = 4;  //系统命令，初始化结束
    private static final int SYS_ALL_CALL = 5;  //系统命令，总召唤
    private static final int SYS_CLOCK_SYNC = 6; //系统命令，时钟同步
    private static final int SYS_RESET_PROCESS = 7; //系统命令，复位进程
    */
    private static HashMap<String,String> map = new HashMap<>();//工艺参数Map

    public static void iec101DecodeProgram(byte[] tcpPayload, Map<String,Float> globalMap, IEC101Config iec101Config,
                                           FvDimensionLayer layer){
        int length = tcpPayload.length;
        switch (length){
            case 1 : decode1(tcpPayload[0]);break;
            case 5 : decode5(tcpPayload);break;
            default:
                if (tcpPayload[0] == 0x68) {
                    decodeN(tcpPayload);
                    break;
                }
        }
    }
    //类型标识偏移
    private static final int TI_OFFSET = 6;
    //变结构限定偏移
    private static final int SQ_NUMBER_OFFSET = 7;
    //传送原因偏移(功能码)
    private static final int TRANSFER_REASON_OFFSET = 8;
    //信息体起始位置偏移
    private static final int INFO_START_OFFSET = 10;
    //时标
    private static final int INFO_TIME_OFFSET = 12;

    private static void decodeN(byte[] tcpPayload) {
        int ti = Byte.toUnsignedInt(tcpPayload[TI_OFFSET]);
        int SQ = tcpPayload[SQ_NUMBER_OFFSET] & 0x80;  //地址连续性，等于0表示地址不连续
        int num = tcpPayload[SQ_NUMBER_OFFSET] & 0x7F; //信息元素个数
        String transferReason = decodeTransferReason(tcpPayload[TRANSFER_REASON_OFFSET]);
        decode101ArtData(tcpPayload,SQ,num,ti);
    }

    private static void decode101ArtData(byte[] tcpPayload,int SQ,int num , int ti) {
        //SQ = 0,地址不连续 SQ = 1，地址连续
        int offsetPerInfo = getOffsetOfEveryInfo(ti);//每个信息体所占的字节数
        if (offsetPerInfo > 0) {
            //前三种情况
            if (SQ == 0) { //[信息体地址(2字节) : 数据(offsetPerInfo个字节)]
                for (int i = 0; i < num; i++) {
                    int offset = (2 + offsetPerInfo) * i + INFO_START_OFFSET;//2是信息体地址长度
                    int address = ByteUtil.bytesToShort(tcpPayload, offset); //不唯一的地址
                    float data = decodeInfoData(tcpPayload,offset + 2,offsetPerInfo,ti);
                    map.put(String.valueOf(address),String.valueOf(data));
                }
            } else {
                //信息体地址(2字节) : [数据(offsetPerInfo个字节)]
                int address = ByteUtil.bytesToShort(tcpPayload, INFO_START_OFFSET); //唯一的地址
                for (int i = 0; i < num; i++) {
                    int offset = 2 + offsetPerInfo * i + INFO_START_OFFSET;//2是信息体地址长度
                    float data = decodeInfoData(tcpPayload,offset + 2,offsetPerInfo,ti);
                    map.put(String.valueOf(address),String.valueOf(data));
                }
            }
        }else{
            //最后一种情况SOE
            int msL = Byte.toUnsignedInt(tcpPayload[INFO_TIME_OFFSET]) & 0xFF;
            int msH = Byte.toUnsignedInt(tcpPayload[INFO_TIME_OFFSET + 1]) & 0xFF;
            int min = Byte.toUnsignedInt(tcpPayload[INFO_TIME_OFFSET + 2]) & 0x3F;
            int hour = Byte.toUnsignedInt(tcpPayload[INFO_TIME_OFFSET + 3]) & 0x1F;
            int day = Byte.toUnsignedInt(tcpPayload[INFO_TIME_OFFSET + 4]) & 0x1F;
            int week = Byte.toUnsignedInt(tcpPayload[INFO_TIME_OFFSET + 4]) & 0xE0;
            int month = Byte.toUnsignedInt(tcpPayload[INFO_TIME_OFFSET + 5]) & 0x0F;
            int year = Byte.toUnsignedInt(tcpPayload[INFO_TIME_OFFSET + 6]) & 0x7F;
            System.out.println(year + "." + month + "." + week + "." + day + "." + hour + "." + min + "." + msH + "." + msL);
        }
        System.out.println(map);//TODO
    }


    private static void decode5(byte[] tcpPayload) {
        int prm = tcpPayload[1] & 0x40;
        int fc = tcpPayload[1] & 0x0F;
        String funCodeMeaning;
        if (prm != 0){
            //报文来自启动站[启动方向]
            switch (fc){
                case 0 : funCodeMeaning = "复位远方链路";break;
                case 1 : funCodeMeaning = "复位用户进程";break;
                case 2 : funCodeMeaning = "发送/确认链路测试功能";break;
                case 3 : funCodeMeaning = "发送/确认用户数据";break;
                case 4 : funCodeMeaning = "发送/无回答用户数据";break;
                case 9 : funCodeMeaning = "请求/响应请求链路状态";break;
                default:funCodeMeaning = "未知启动方向功能码操作";
            }
        }else{
            //报文来自从动站[从动方向]
            switch (fc){
                case 0 : funCodeMeaning = "确认：认可";break;
                case 1 : funCodeMeaning = "确认：否定认可";break;
                case 11 : funCodeMeaning = "响应：链路状态";break;
                default: funCodeMeaning = "未知从动方向功能码操作";
            }
        }
        System.out.println(funCodeMeaning);
    }

    private static void decode1(byte b) {

    }

    private static String decodeTransferReason(byte payload){
        String reason = "错误：未知传送原因";
        switch (payload){
            case 0 : reason = "未用";break;
            case 1 : reason = "周期、循环（遥测）";break;
            case 2 : reason = "背景扫描（遥信）（遥测）";break;
            case 3 : reason = "突发（自发）（遥信）（遥测）";break;
            case 4 : reason = "初始化完成";break;
            case 5 : reason = "请求或者被请求（遥信被请求）（遥测被请求）";break;
            case 6 : reason = "激活";break;
            case 7 : reason = "激活确认";break;
            case 8 : reason = "停止激活";break;
            case 9 : reason = "停止激活确认";break;
            case 10 : reason = "激活终止";break;
            case 13 : reason = "文件传输";break;
            case 20 : reason = "响应站召唤（总召唤）";break;
            case 0x2c : reason = "未知的召唤标识";break;
            case 0x2d : reason = "未知的传送原因";break;
            case 0x2e : reason = "未知的应用服务数据单元公共地址";break;
            case 0x2f : reason = "未知的信息对象地址";break;
            case 0x30 : reason = "遥控执行软压板状态错误";break;
            case 0x31 : reason = "遥控执行时间戳错误";break;
            case 0x32 : reason = "遥控执行数字签名认证错误";break;
            default:
                System.err.println("未知101协议传送原因：" + payload);
        }
        return reason;
    }


    private static int getOffsetOfEveryInfo(int ti){
        switch (ti){
            //遥信
            case 0x01 : return 1;
            case 0x03 : return 1;
            case 0x14 :
            case 0x0e :
                return 8;
            //遥测
            case 0x09 : return 3;
            case 0x0a :
            case 0x0c :
                return 6;
            case 0x0b : return 3;
            case 0x0d : return 5;
            case 0x15 : return 2;
            //KWH
            case 0x0f : return 5;
            case 0x10 : return 8;
            case 0x25 : return 12;
            default: return -1;//SOE
        }
    }

    private static float decodeInfoData(byte[] tcpPayload,int offset,int offsetPerInfo,int ti){
        switch (offsetPerInfo){
            case 1 :
                return tcpPayload[offset];
            case 5 :
                if (ti == 0x0d){//遥测
                    return (float) (tcpPayload[offset] + tcpPayload[offset + 1] / 100.0);
                } else{//KWH
                    return 0f;
                }
        }
        return -1f;
    }
}
