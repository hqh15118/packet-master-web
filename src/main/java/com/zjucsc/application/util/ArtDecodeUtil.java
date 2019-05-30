package com.zjucsc.application.util;

public class ArtDecodeUtil {

    public static float fourByteArrToFloat(byte[] payload,int offset){
        return 0F;
    }

    /**
     * @param payload 负载
     * @param offset 偏移
     * @param bitBucket 桶位，第几个bit是开关量
     * @return
     */
    public static float byteToBit(byte[] payload,int offset,int bitBucket){
        byte b = payload[offset];
        int i = 1 << bitBucket;
        return (Byte.toUnsignedInt(b) & i) != 0 ? 1 : 0;
    }

    /**
     * 两个字节的整型
     * @param payload 负载
     * @param offset 偏移
     * @return
     */
    public static short twoBytesArrToShort(byte[] payload,int offset){
        return ByteUtil.bytesToShort(payload,offset);
    }

    public static int fourBytesArrToInt(byte[] payload , int offset){
        return ByteUtil.bytesToInt(payload,offset);
    }

}
