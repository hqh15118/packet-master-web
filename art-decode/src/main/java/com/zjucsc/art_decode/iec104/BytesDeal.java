package com.zjucsc.art_decode.iec104;

public class BytesDeal {

    //将字节数组（四个元素）转换为IEEE 754标准32位单精度浮点数
    //第一个元素byte0为最高字节（大端序，字节最高位为符号位sign）
    public static float BytesToSingle(byte byte0, byte byte1,byte byte2,byte byte3) {
        int sign = 0;
        float fraction = 0;
        int exponent = 0;
        if ((byte0 & 0xff) >= 128) {
            sign = -1;
        }
        else {
            sign = 1;
        }
        int temp;
        if ((byte1 & 0xff) >= 128) {
            temp = 1;
        }
        else {
            temp = 0;
        }
        exponent = ((byte0 & 0xff) % 128) * 2 + temp;
        float[] tempdata = new float[3];
        tempdata[0] = byte3 & 0xff;
        tempdata[1] = byte2 & 0xff;
        tempdata[2] = byte1 & 0xff;
        fraction = (tempdata[2] - temp * 128 + 128) / 128 + tempdata[1] / (128 * 256) + tempdata[0] / (128 * 256 * 256);
        float result = 0f;
        if (exponent == 0 && fraction != 0) {
            result = (float) (sign * (fraction - 1) * Math.pow(2, -126));
            return result;
        }
        if (exponent == 0 && fraction == 0f) {
            result = (float) 0.0;
            return result;
        }
        if (sign == 0f && exponent == 255 && fraction == 0f) {
            result = (float) 1111.11;
            return result;
        }
        if (sign == 1 && exponent == 255 && fraction == 0) {
            result = (float) -1111.11;
            return result;
        } else {
            result = (float) (sign * fraction * Math.pow(2, exponent - 127));
            return result;
        }
    }
}