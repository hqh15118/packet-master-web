package com.zjucsc.application.system.art.s7comm;

import com.zjucsc.art_decode.AttackType;
import com.zjucsc.art_decode.IArtDecode;
import com.zjucsc.application.util.CommonUtil;

import java.util.List;
import java.util.Map;

import static com.zjucsc.art_decode.AttackType.HAZARD_ART;
import static com.zjucsc.art_decode.AttackType.TAMPER_ATTACK;

@SuppressWarnings("unchecked")
public class S7CommDecode implements IArtDecode {


    @Override
    public Map<String, Float> decode(Map<String, Float> map, byte[] bytes, Object... objects) {
        return decode_tech(bytes,map);
    }

    public void decode1(Map<String, Float> control_map, byte[] bytes, Object... objects) {
        String control = null;
        //判断条件为：从TPKT之后报文第9位即S7第二位ROSCTR=1类型为JOB，且TPKT之后第18位为05，代表功能码为WriteVar
        if(bytes[17]==0x05 && bytes[8]==0x01) {
            switch (bytes[30]) {
                case 0x06:
                    control = "开启第一个船闸";
                    control_map.put("第一个船闸状态量", 1f);
                    break;
                case 0x07:
                    control = "关闭第一个船闸";
                    control_map.put("第一个船闸状态量", 0f);
                    break;
                case (byte)0xf6:
                    control = "开启第二个船闸";
                    control_map.put("第二个船闸状态量", 1f);
                    break;
                case (byte)0xf7:
                    control = "关闭第二个船闸";
                    control_map.put("第二个船闸状态量", 0f);
                    break;
                case (byte)0xe6:
                    control = "开启第三个船闸";
                    control_map.put("第三个船闸状态量", 1f);
                    break;
                case (byte)0xe7:
                    control = "关闭第三个船闸";
                    control_map.put("第三个船闸状态量", 0f);
                    break;
                case (byte)0xd6:
                    control = "开启第四个船闸";
                    control_map.put("第四个船闸状态量", 1f);
                    break;
                case (byte)0xd7:
                    control = "关闭第四个船闸";
                    control_map.put("第四个船闸状态量", 0f);
                    break;
                case (byte)0xc6:
                    control = "开启第五个船闸";
                    control_map.put("第五个船闸状态量", 1f);
                    break;
                case (byte)0xc7:
                    control = "关闭第五个船闸";
                    control_map.put("第五个船闸状态量", 0f);
                    break;
                case (byte)0xb6:
                    control = "开启第六个船闸";
                    control_map.put("第六个船闸状态量", 1f);
                    break;
                case (byte)0xb7:
                    control = "关闭第六个船闸";
                    control_map.put("第六个船闸状态量", 0f);
                    break;
            }
        }
    }




    @Override
    public String protocol() {
        return "s7comm";
    }

    @Override
    public List<AttackType> attackDecode(List<AttackType> list, byte[] bytes, Object... objects) {
        StringBuilder sb = CommonUtil.getGlobalStringBuilder();
        sb.delete(0,sb.length());
        Map<String,Float> control_map = (Map)objects[0];
        if(control_map.get("第一个船闸状态量")==1 && control_map.get("第二个船闸状态量")==1){
            list.add(HAZARD_ART);
            sb.append("逆工艺操作！一二级船闸违法连通/");
        }

        if(control_map.get("第二个船闸状态量")==1 && control_map.get("第三个船闸状态量")==1){
            list.add(HAZARD_ART);
            sb.append("逆工艺操作！二三级船闸违法连通/");
        }

        if(control_map.get("第三个船闸状态量")==1 && control_map.get("第四个船闸状态量")==1){
            list.add(HAZARD_ART);
            sb.append("逆工艺操作！三四级船闸违法连通/");
        }

        if(control_map.get("第四个船闸状态量")==1 && control_map.get("第五个船闸状态量")==1){
            list.add(HAZARD_ART);
            sb.append("逆工艺操作！四五级船闸违法连通/");
        }

        if(control_map.get("第五个船闸状态量")==1 && control_map.get("第六个船闸状态量")==1){
            list.add(HAZARD_ART);
            sb.append("逆工艺操作！五六级船闸违法连通/");}

        if(control_map.get("第一个船闸状态量") + control_map.get("第二个船闸状态量") + control_map.get("第三个船闸状态量")
                + control_map.get("第四个船闸状态量") + control_map.get("第五个船闸状态量") + control_map.get("第六个船闸状态量") >= 3){
            list.add(HAZARD_ART);
            sb.append("逆工艺攻击告警！");
        }

        //当出现setupCommunication，requestDownload，DownloadBlock，DownloadEnded，PI-Server功能码时，视为对PLC程序修改，未作授权视为攻击！
        if((bytes[18]==0x1a) || (bytes[18]==0x1b) || (bytes[18]==0x1c) || (bytes[18]==0x28)){
            list.add(TAMPER_ATTACK);
            sb.append("注意！正在修改PLC数据，未经授权视为攻击/");
        }
        return list;
    }

    public static Float bytesTofloat(byte[] payload, int offset) {// 解析4个字节中的数据，按照IEEE754的标准
        byte[] data = Bytecut.Bytecut(payload, offset,4);
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
        if((bytes[0] & 0x02)==(byte)0x02) {
            data2 = 1f;
        }
        else{
            data2 = 0;
        }
        if ((bytes[0] & 0x04)==(byte)0x04)
        {
            data1 = 1f;
        }
        else
        {
            data1 = 0;
        }
        if((bytes[1] & 0x20)==(byte)0x20) {
            data4 = 1f;
        }
        else{
            data4 = 0;
        }
        if ((bytes[1] & 0x40)==(byte)0x40)
        {
            data3 = 1f;
        }
        else
        {
            data3 = 0;
        }
        return new float[]{data1,data2,data3,data4};
    }

    private Map<String, Float> decode_tech (byte[] payload,Map<String, Float> techmap)
    {
        decode1(techmap,payload);

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
            return techmap;
        }
        else if(payload.length ==100)
        {
            float level1 = bytesTofloat(payload,73);
            float level2 = bytesTofloat(payload,77);
            float level3 = bytesTofloat(payload,81);
            float level4 = bytesTofloat(payload,85);
            float level5 = bytesTofloat(payload,89);
            float level6 = bytesTofloat(payload,93);
            techmap.put("压差0_1",level1);
            techmap.put("压差1_2",level2);
            techmap.put("压差2_3",level3);
            techmap.put("压差3_4",level4);
            techmap.put("压差4_5",level5);
            techmap.put("压差5_6",level6);
            return techmap;
        }
        else if(payload.length == 47)
        {
            float level0 = bytesTofloat(payload ,40);
            techmap.put("水位0",level0);
            return techmap;
        }
        return techmap;
    }
}
