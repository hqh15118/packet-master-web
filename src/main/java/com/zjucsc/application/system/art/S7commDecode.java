package com.zjucsc.application.system.art;

import com.zjucsc.AttackType;
import com.zjucsc.Bytecut;
import com.zjucsc.IArtDecode;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class S7commDecode implements IArtDecode, Serializable {


    @Override
    public Map<String, Float> decode(Map<String, Float> map, byte[] bytes, Object... objects) {
        return decode_tech(bytes,map);
    }

    @Override
    public String protocol() {
        return "s7comm";
    }

    @Override
    public List<AttackType> attackDecode(List<AttackType> list, byte[] bytes, Object... objects) {
        return null;
    }

    public static Float bytesTofloat(byte[] payload, int offset) {// 解析4个字节中的数据，按照IEEE754的标准
        byte[] data = Bytecut.Bytecut(payload, offset,4);
        int s = 0;// 浮点数的符号
        float f = 0;// 浮点数
        int e = 0;// 指数
        if ((data[3] & 0xff) >= 128) {// 求s
            s = -1;
        } else {
            s = 1;
        }
        int temp = 0;// 指数位的最后一位
        if ((data[2] & 0xff) >= 128) {
            temp = 1;
        } else
            temp = 0;
        e = ((data[3] & 0xff) % 128) * 2 + temp;// 求e
        // f=((data[2]&0xff)-temp*128+128)/128+(data[1]&0xff)/(128*256)+(data[0]&0xff)/(128*256*256);
        float[] data2 = new float[3];
        data2[0] = data[0] & 0xff;
        data2[1] = data[1] & 0xff;
        data2[2] = data[2] & 0xff;
        f = (data2[2] - temp * 128 + 128) / 128 + data2[1] / (128 * 256)
                + data2[0] / (128 * 256 * 256);
        float result = 0;
        if (e == 0 && f != 0) {// 次正规数
            result = (float) (s * (f - 1) * Math.pow(2, -126));
            
            return result;
        }
        if (e == 0 && f == 0) {// 有符号的0
            result = (float) 0.0;
            return result;
        }
        if (s == 0 && e == 255 && f == 0) {// 正无穷大
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

    private static float[] valve_state(byte[] bytes)
    {
        float data1,data2,data3,data4;
        if(bytes[0]==0x02) {
            data1 = 0;
            data2 = 1f;
        }
        else if (bytes[0]==0x04)
        {
            data1 = 1f;
            data2 = 0;
        }
        else if(bytes[0]==0x06)
        {
            data1 =1f;
            data2 =1f;
        }
        else
        {
            data1 = 0;
            data2 = 0;
        }
        if(bytes[1]==0x20) {
            data3 = 0;
            data4 = 1f;
        }
        else if (bytes[1]==0x40)
        {
            data3 = 1f;
            data4 = 0;
        }
        else if(bytes[1]==0x60)
        {
            data3 =1f;
            data4 =1f;
        }
        else
        {
            data3 = 0;
            data4 = 0;
        }
        return new float[]{data1,data2,data3,data4};
    }

    private static Map<String, Float> decode_tech (byte[] payload,Map<String, Float> techmap)
    {
        if(payload.length == 64 )
        {
            float level1 = bytesTofloat(payload,40);
            float level2 = bytesTofloat(payload,44);
            float level3 = bytesTofloat(payload,48);
            float level4 = bytesTofloat(payload,52);
            float level5 = bytesTofloat(payload,56);
            float level6 = bytesTofloat(payload,60);
            techmap.put("水位1", level1);
            techmap.put("水位2", level2);
            techmap.put("水位3", level3);
            techmap.put("水位4", level4);
            techmap.put("水位5", level5);
            techmap.put("水位6", level6);
        }
        else if(payload.length == 67)
        {
            float level1 = bytesTofloat(payload,43);
            float level2 = bytesTofloat(payload,47);
            float level3 = bytesTofloat(payload,51);
            float level4 = bytesTofloat(payload,55);
            float level5 = bytesTofloat(payload,59);
            float level6 = bytesTofloat(payload,63);
            techmap.put("压差0_1",level1);
            techmap.put("压差1_2",level2);
            techmap.put("压差2_3",level3);
            techmap.put("压差3_4",level4);
            techmap.put("压差4_5",level5);
            techmap.put("压差5_6",level6);
        }
        else if(payload.length == 47)
        {
            float level0 = bytesTofloat(payload ,40);
            techmap.put("水位0",level0);
        }
        else if(payload.length ==75)
        {
            byte[] head = Bytecut.Bytecut(payload,39,4);
            byte[] bytes1,bytes2;
            if(head[3]==(byte)0xff)
            {
                bytes1 = Bytecut.Bytecut(payload, 43, 2);
                bytes2 = Bytecut.Bytecut(payload, 73, 2);
            }
            else if(head[2]==(byte)0xff)
            {
                bytes1 = Bytecut.Bytecut(payload, 42, 2);
                bytes2 = Bytecut.Bytecut(payload, 72, 2);
            }
            else
            {
                bytes1 = Bytecut.Bytecut(payload, 41, 2);
                bytes2 = Bytecut.Bytecut(payload, 71, 2);
            }
            float[] f1=valve_state(bytes1);
            float[] f2=valve_state(bytes2);
            if(head[3]==(byte)0xff)
            {
                techmap.put("一号左闸开",f1[0]);
                techmap.put("一号左闸关",f1[1]);
                techmap.put("一号右闸开",f1[2]);
                techmap.put("一号右闸关",f1[3]);
                techmap.put("二号左闸开",f2[0]);
                techmap.put("二号左闸关",f2[1]);
                techmap.put("二号右闸开",f2[2]);
                techmap.put("二号右闸关",f2[3]);
                return techmap;
            }
            else if(head[2]==(byte)0xff)
            {
                techmap.put("三号左闸开",f1[0]);
                techmap.put("三号左闸关",f1[1]);
                techmap.put("三号右闸开",f1[2]);
                techmap.put("三号右闸关",f1[3]);
                techmap.put("四号左闸开",f2[0]);
                techmap.put("四号左闸关",f2[1]);
                techmap.put("四号右闸开",f2[2]);
                techmap.put("四号右闸关",f2[3]);
            }
            else if(head[1]==(byte)0xff)
            {
                techmap.put("五号左闸开",f1[0]);
                techmap.put("五号左闸关",f1[1]);
                techmap.put("五号右闸开",f1[2]);
                techmap.put("五号右闸关",f1[3]);
                techmap.put("六号左闸开",f2[0]);
                techmap.put("六号左闸关",f2[1]);
                techmap.put("六号右闸开",f2[2]);
                techmap.put("六号右闸关",f2[3]);
            }
            else
            {
                return techmap;
            }
        }
        else {
            return techmap;
        }
        return techmap;
    }
}
