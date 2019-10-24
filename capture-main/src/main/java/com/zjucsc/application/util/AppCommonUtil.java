package com.zjucsc.application.util;

import com.zjucsc.application.statistic.StatisticsData;
import com.zjucsc.common.util.ByteUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

public class AppCommonUtil {


    /**
     * 用于初始化工艺参数需要的数据
     * 1.StatisticsData.initArtArgs(artArg);
     * 【ConcurrentHashMap<String, LinkedList<String>> ART_INFO】 ==> 发送给前端的
     * 2.后端解析完成后往里面put的
     * @param artArg 工艺参数名称
     */
    public static void initArtMap2Show(String artArg){
        StatisticsData.initArtMap(artArg);
    }

    /*************************************
     * 对时
     ************************************/
    private static byte[] getPacketData(){
        Calendar calendar = Calendar.getInstance();
        short year = (short) calendar.get(Calendar.YEAR);
        byte month = (byte)(calendar.get(Calendar.MONTH)+1);
        byte day = (byte)calendar.get(Calendar.DAY_OF_MONTH);
        byte hour = (byte)calendar.get(Calendar.HOUR_OF_DAY);
        byte minute = (byte)calendar.get(Calendar.MINUTE);
        byte second = (byte)calendar.get(Calendar.SECOND);
        short millSecond = (short)calendar.get(Calendar.MILLISECOND);
        return ByteUtil.contractBytes(23,
                new byte[]{0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xa0},
                new byte[]{0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xa0},
                new byte[]{(byte) 0x89,0x07},
                ByteUtil.shortToByte(year),new byte[]{month,day,hour,minute,second},ByteUtil.shortToByte(millSecond));
    }

    public static boolean timeCheck(String interfaceName){
        byte[] packetData = getPacketData();
        return TsharkUtil.sendPacket(interfaceName,packetData);
    }

}
