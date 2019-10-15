package com.zjucsc.art_decode.mms;

import com.zjucsc.art_decode.artconfig.MMSConfig;
import com.zjucsc.art_decode.base.BaseArtDecode;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.HashMap;
import java.util.Map;

public class MMSDecode extends BaseArtDecode<MMSConfig> {

    /**
     *
     * @param mmsConfig  配置类
     * @param globalMap  工艺参数map key 工艺参数名字，value 工艺参数的值，float
     * @param payload    MMS层开始的所有数据一并灌入
     * @param obj        额外的信息
     * @return
     */


    private Map<String, String> MMS_title = new HashMap<String, String>();
    private Map<String, String> MMSData_map = new HashMap<String, String>();

    private int KeyNum = 1;                                                               //表示为第几个items

    private void putMMS_title(byte[] bytes) {

        int start = 0;
        int bechar;
        int invokeID = 0;
        StringBuilder Key = new StringBuilder();
        StringBuilder Value = new StringBuilder();
        for (int m = 0; m < bytes.length; m++) {                                               //从第一个字符开始判断，一直判断到最后一个字符
            if (bytes[m] == 0x02 && bytes[m + 1] == 0x03) {
                invokeID = (int) bytes[m + 2] * 256 * 256 + (int) bytes[m + 3] * 256 + (int) bytes[m + 4];
            }

            if (bytes[m] == 0x1a && (bytes[m + 1] == 0x0f || bytes[m + 1] == 0x0e)) {                                 //判断domain-specific数据类型特征byte之后，即为需要保存下来的ID
                start = m + 2;                                                             //记录下来出现首个domain-specific的位置
                break;                                                                 //跳出，即只舍弃头部冗余报文

            }
        }

        int CountNum = 1;
        while (bytes[start] != 0x1a) {                                            //当出现0x1a时候，代表出现分隔
            bechar = (int) bytes[start];                                          //把byte转int
            char ch = (char) bechar;                                              //把int转char就按照ascII码转换
            Key.append(ch);                                                            //把char写在string后面累加，组成字符串
            start++;                                                              //偏移量+1
        }
        for (int a = start + 2; a < bytes.length; a++) {                                  //从判断的start开始，遍历整个数组

            if (bytes[a] == 0x0f || bytes[a] == 0x00) {                                           //如果既出现0x1a也出现0x0f，代表其一个items读取完毕，Key的num要加1
                KeyNum++;                                                             //items 计数加一
                CountNum = 1;
                MMS_title.put(Key + "ID" + invokeID, Value.toString());                          //把保存下来的key与value传递到map中
                Key = new StringBuilder();                                                             //清空key
                Value = new StringBuilder();                                                           //清空value
            }
            else {
                if(CountNum == 1) {
                    Key.append(KeyNum);                                                        //把当前items数记录到KeyNum中
                    CountNum = 0;
                }
                else{
                     // while ((bytes[start] > 0x2F && bytes[start] < 0x3A) || (bytes[start] > 0x40 && bytes[start] < 0x5B) || (bytes[start] > 0x60 && bytes[start] < 0x7B) || bytes[start] == 0x24) {         //判断是否为数字，字母和$符号，如果符合即记录下来                     //判断出现字母是否停止,或者是出现美元符
                    bechar = (int) bytes[a];                                      //把byte转int
                    char chValue = (char) bechar;                                     //把int转char就按照ascII码转换
                    Value.append(chValue);                                                 //把char写在string后面累加，组成字符串
                        // start++;                                                          //偏移量+1
                    //}
                }
            }
        }
        MMS_title.put(Key + "ID" + invokeID, Value.toString());                          //把保存下来的key与value传递到map中
        KeyNum++;                                                             //items 计数加一
        // System.out.println(MMS_title);
    }



      /*
    if(destination.ip == 192.168.0.105){
    run  Map<String, Float> MMSData_map, byte[] bytes
    */

    private void putMMSData_Map(byte[] bytes) {
        int itemsLen = 0;
        int itemsNum = 0;
        int invokeID = 0;
        int Util = 0;
        int startUtil = 0;
        int MMSStart = 0;
        int totalItems = itemsNum;
        int BitStringNum = 1;
        int boolNum = 1;
        int FloatPointNum = 1;
        int UTCNum = 1;
        int SubLength = 0;
        for(int i=0; i<bytes.length;i++) {                                                          //根据判断，这两个字节代表的是对于请求循环数据的反馈,跳过4个字节（后两个意义不明）
            if (Byte.toUnsignedInt(bytes[i]) == 0xa1 && Byte.toUnsignedInt(bytes[i + 1]) == 0x82) {
                MMSStart = i + 2;
                break;
            }
        }

            for (int m = MMSStart; m < bytes.length; m++) {
                if (bytes[m] == 0x02 && bytes[m + 1] == 0x03) {                                                   //从第三个byte开始遍历找到invoke前代表PDU类文件的2bytes标志位0x02与0x03
                    invokeID = (int) bytes[m + 2] * 256 * 256 + (int) bytes[m + 3] * 256 + (int) bytes[m + 4];     //后面三位为invokeID，换算成10进制数字，作为其invokeID。该ID是与工程师站到PLC的request报文完全对应，表示的是对应申请的数据回复信息。
                    // System.out.println(invokeID);
                    Util = m + 4;                                                                         //从invokeID之后开始遍历
                    break;
                }
            }

            // String MMSstring = "";

            if (Byte.toUnsignedInt(bytes[Util + 1]) == 0xa4) {                                                                    //根据下一个字节是否为0x04判断开始状态
                for (int j = Util + 1; j < bytes.length - 1; j++) {

                    if (Byte.toUnsignedInt(bytes[j]) == 0xa1) {                                                                 //0x02代表2层包裹，之后进行周期性计数
                        for (int q = j; q < bytes.length; q++) {
                            if (Byte.toUnsignedInt(bytes[q]) == 0x83 || Byte.toUnsignedInt(bytes[q]) == 0x84 ||Byte.toUnsignedInt( bytes[q] )== 0x87 || Byte.toUnsignedInt(bytes[q] )== 0xa2) {   //判断在第一层包装0xa1之后，是否出现了几个标志位（2层包装，bool，bit，float），出现代表开始报告的数据
                                startUtil = q;                                                            //记录下子层数据的起始位
                                j = q + 1;
                                break;                                                                    //跳出循环降低算法复杂度
                            }
                        }
                    }

                    if (Byte.toUnsignedInt(bytes[j] )== 0xa2) {             //判断其后的两位是不是之后周期性出现，若出现则该模块的2层包裹完成。
                        //if (itemsNum == 1) {
                        //  itemsLen = j - startUtil + 1;                                        //计算出开始的子单元模块的长度
                        //}
                        itemsNum++;                                                                      //子模块数+1
                    }
                }
                int start = 0;
                if(startUtil < 100) {
                     start = startUtil;  //+ (totalItems - itemsNum) * itemsLen;                       //当前子items的起始位置设置
                }
                else start = 40;
                while (itemsNum > 0) {
                    int k = start;
                    while (Byte.toUnsignedInt(bytes[k] )!= 0xa2 && k < bytes.length - 1) {
                        if (Byte.toUnsignedInt(bytes[k]) == 0x83) {                                                    //boolean量数据读取
                            int length = bytes[k + 1];
                            if (bytes[k + 2] == 0x00) {
                                String MMSboolean = "0";
                                MMSboolean += Byte.toUnsignedInt(bytes[k + 2]);
                                MMSData_map.put("boolean" + boolNum + "ID" + invokeID, MMSboolean);
                            } else {
                                String MMSboolean = "0";
                                MMSboolean += Byte.toUnsignedInt(bytes[k + 2]);
                                MMSData_map.put("boolean" + boolNum + "ID" + invokeID, MMSboolean);
                            }
                            boolNum++;
                            k = k + 2 + length;
                        }
                        else if (Byte.toUnsignedInt(bytes[k]) == 0x84) {                                                     //bit-string数据读取
                            int length = bytes[k + 1];
                            int padding = (int) bytes[k + 2];
                            String MMStrings = "";
                            MMStrings +=  Byte.toUnsignedInt(bytes[k + 3]);
                            MMStrings +=  Byte.toUnsignedInt(bytes[k + 4]);
                            MMSData_map.put("Bit-String" + padding + BitStringNum + "ID" + invokeID, MMStrings);
                            BitStringNum++;
                            k = k + 2 + length;
                        }
                        else if (Byte.toUnsignedInt(bytes[k]) == 0x87) {                                                     //floating-point数据读取
                            int length = bytes[k + 1];
                            String MMStrings = "";
                            for(int z=2;z<6;z++){
                                MMStrings += Byte.toUnsignedInt(bytes[k + z]);
                            }
                            MMSData_map.put("Floating-Point" + FloatPointNum + "ID" + invokeID, MMStrings);
                            FloatPointNum++;
                            k = k + 2 + length;
                        }
                        else if (Byte.toUnsignedInt(bytes[k]) == 0xa2) {
                            SubLength = bytes[k + 1];                                             //此为structure包装层2，具体数据还是要扒,因此继续往下
                            k = k + 2;
                        }

                        else if (Byte.toUnsignedInt(bytes[k]) == 0x91) {                                                      //此为时间戳的记录
                            //int length = bytes[k+1];
                            StringBuilder MMStrings = new StringBuilder(" ");
                            StringBuilder MMString1 = new StringBuilder(" ");
                            StringBuilder MMString2 = new StringBuilder(" ");
                            for(int j = 2;j<10;j++){
                                MMStrings.append(Byte.toUnsignedInt(bytes[k + j]));
                                MMString1.append(Byte.toString(bytes[k + j]));
                                MMString2.append(String.format("%02X", bytes[k + j]));
                            }
                            MMSData_map.put("UTC-Time" + UTCNum + "ID" + invokeID, MMStrings.toString());
                            UTCNum ++;
                            k = k + 10;
                            continue;

                        }
                        else k++;
                    }
                    k ++;
                    start = k;
                    itemsNum--;                                                                  //跑完一次之后，代表着一个子模块跑完
                }
                // System.out.println(MMSData_map);
            }
    }


    @Override
    public Map<String, Float> decode(MMSConfig mmsConfig, Map<String, Float> globalMap, byte[] payload, FvDimensionLayer layer, Object... obj) {

        if(layer.ip_dst[0] .equals(mmsConfig.getIp())){
            putMMSData_Map(payload);
        }
        else if(layer.ip_src[0] .equals(mmsConfig.getIp())){
            putMMS_title(payload);
        }
        String substr = "", substr2 = "", substrr2 = "";
        String value1, value2;
        int stop = 0;
        int start = 0;
        String key;
        for(Map.Entry<String,String> entry : MMS_title.entrySet()){//得到配置信息key值
            value1 = entry.getValue();//并把其中value保存下来
            key = entry.getKey();
            for(int i=0;i<key.length();i++){                                                      //遍历string中每一个字符，判断是否出现ID字母
                if(key.charAt(i)=='I'&& key.charAt(i+1)=='D'){
                    substr = key.substring(i+2);                                                  //出现则提取后面，为invokeID
                    break;
                }
            }
            //System.out.println(MMSData_map);
            String key2;
            for(Map.Entry<String,String> entry1 : MMSData_map.entrySet()){//得到读取信息的key值
                key2 = entry1.getKey();
                for(int j=0;j<key2.length();j++){                                                 //遍历string中每一个字符，判断是否出现ID字母
                    if(key2.charAt(j)=='I'&&key2.charAt(j+1)=='D'){
                        substr2 = key2.substring(j+2);                                            //出现则提取后面，为invokeID
                        stop = j;
                        break;
                    }
                }
                if(substr .equals(substr2)){                                                            //判断invokeID是否相同
                    value2 = entry1.getValue();                                               //相同则把数据value保存下来
                    //System.out.println(key2);
                    /* if(mmsConfig.getTech_param()!= ""){
                        globalMap.put(mmsConfig.getTech_param(), Float.parseFloat(value2));
                        callback(mmsConfig.getTech_param(), Float.parseFloat(value2), layer);
                        start += 1;
                        // return null;
                    }
                    else{*/
                    substrr2 = key2.substring(0,stop);
                    globalMap.put(value1 + "&" + substrr2, Float.parseFloat(value2));               //放入到新的map当中

                    callback(mmsConfig.getTech_param(), Float.parseFloat(value2), layer);
                    start += 1;
                        // return null;
                    //}

                    //start ++;                                                                     //为了防止出现想通过的key值，在这加一个map的计数器（其中一个invokeID可能对应好几个value）
                }
                else continue;                                                                    //如果不相等，则继续寻找下一个数据value
            }
        }
        return null;
    }

    @Override
    public String protocol() {
        return "mms";
    }
}
