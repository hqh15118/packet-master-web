package com.zjucsc.common.common_util;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Bytecut {

    private static ThreadLocal<ByteBuffer> byteToByteBuffer = ThreadLocal.withInitial(() ->
    {
        return ByteBuffer.allocate(4);
    });
    public Bytecut() {
    }
    public static byte[] Bytecut( byte[] bytes, int offset,int len){
        if(offset<bytes.length) {
            if (len != -1) {
                return Arrays.copyOfRange(bytes, offset, offset + len);
            } else {
                return Arrays.copyOfRange(bytes, offset, bytes.length);
            }
        }
        return null;
    }
    public static Float BytesTofloat(byte[] payload, int offset) {// 解析4个字节中的数据，按照IEEE754的标准
        byte[] data = Bytecut.Bytecut(payload, offset, 4);
        int s = 0;// 浮点数的符号
        float f = 0;// 浮点数
        int e = 0;// 指数
        if ((data[0] & 0xff) >= 128) {// 求s
            s = -1;
        } else {
            s = 1;
        }
        int temp;// 指数位的最后一位
        if ((data[1] & 0xff) >= 128) {
            temp = 1;
        } else
            temp = 0;
        e = ((data[0] & 0xff) % 128) * 2 + temp;// 求e
        // f=((data[2]&0xff)-temp*128+128)/128+(data[1]&0xff)/(128*256)+(data[0]&0xff)/(128*256*256);
        float[] data2 = new float[3];
        data2[0] = data[3] & 0xff;
        data2[1] = data[2] & 0xff;
        data2[2] = data[1] & 0xff;
        f = (data2[2] - temp * 128 + 128) / 128 + data2[1] / (128 * 256) + data2[0] / (128 * 256 * 256);
        float result = 0;
        if (e == 0 && f != 0) {// 次正规数
            result = (float) (s * (f - 1) * Math.pow(2, -126));
            return result;
        }
        if (e == 0 && f == 0f) {// 有符号的0
            result = (float) 0.0;
            return result;
        }
        if (s == 0f && e == 255 && f == 0f) {// 正无穷大
            result = (float) 1111.11;
            return result;
        }
        if (s == 1 && e == 255 && f == 0) {// 负无穷大
            result = (float) -1111.11;
            return result;
        } else {
            result = (float) (s * f * Math.pow(2, e - 127));
            return result;
        }
    }
}