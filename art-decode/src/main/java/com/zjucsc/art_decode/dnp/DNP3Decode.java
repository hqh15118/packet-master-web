package com.zjucsc.art_decode.dnp;

import com.zjucsc.art_decode.base.BaseArtDecode;

import java.util.*;

public class DNP3Decode extends BaseArtDecode<DNP3Config> {


    //DNP3tech为配置类，tech_map为最后的返回的“工艺参数名-数值”键值对，load是tcppayload（报文）
    public  Map<String,Float> decode_tech(DNP3Config DNP3tech , Map<String,Float> tech_map , byte[] load)
    {
        byte[] dnp3load = load;     //获取报文
        int start = 0;
        int stop = 0;
        int infobyte = 0;
        int length,length1,bytes;
        float output = 0f;
        //报文中第3个字节：链路报文长度
        length=Byte.toUnsignedInt(dnp3load[2]);
        length1=length+5;
        byte[] editedload=new byte[length1];
        int i=0,j=10,n=0,m=0,x;
        //计算总的报文长度
        length= (length - 5) / 16 * 2 + length + 2 + 3;
        if ((length - 5) % 16 > 0){
            length = length + 2;
        }
        //前10个字节直接拷贝
        for (i=0;i<=9;i++){
            editedload[i]=dnp3load[i];
        }
        //从第11个字节开始，每隔16个字节去除CRC校验码
        for(i=10;i<=(length-3);i++){
            if(((i-8)%18!=0)&&((i-9)%18!=0)){
                editedload[j]=dnp3load[i];
                j=j+1;
            }
        }
        //判断链路控制字节：子站发给从站，功能码为4（帧类型为发送/不期待回信）
        if ((editedload[3]&0x8f)==0x04){
            //应用层控制字节：表示接收到报文时，需要给予确认
            if ((editedload[11]&0x20)==0x20){
                //应用层功能码：功能码为129表示响应
                if (Byte.toUnsignedInt(editedload[12])==0x81){
                    if(j>=23) {
                        infobyte = 15;
                        do {
                            //模拟量：对象组=30
                            if (editedload[infobyte] == 0x1e) {
                                //对象变体
                                if (editedload[(infobyte + 1)] == 0x04) {
                                    if (editedload[(infobyte + 2)] == 0x01) {
                                        start = Byte.toUnsignedInt(editedload[(infobyte + 3)]) + (Byte.toUnsignedInt(editedload[(infobyte + 4)]) << 8);
                                        stop = Byte.toUnsignedInt(editedload[(infobyte + 5)]) + (Byte.toUnsignedInt(editedload[(infobyte + 6)]) << 8);
                                        for (n = 0; n <= (stop - start); n++) {
                                            if ((n + start) == DNP3tech.getindex() && DNP3tech.getObjGroup() == 0x1e) {
                                                output = (float) (Byte.toUnsignedInt(editedload[(infobyte + 7 + 2 * n)]) + (Byte.toUnsignedInt(editedload[(infobyte + 8 + 2 * n)]) << 8));
                                                tech_map.put(DNP3tech.getTag(), output);
                                            }
                                        }
                                        infobyte = infobyte + 6 + (stop - start + 1) * 2;
                                    }
                                }
                                //对象变体
                                else if (editedload[(infobyte + 1)] == 0x02) {
                                    if (editedload[(infobyte + 2)] == 0x01) {
                                        start = Byte.toUnsignedInt(editedload[(infobyte + 3)]) + (Byte.toUnsignedInt(editedload[(infobyte + 4)]) << 8);
                                        stop = Byte.toUnsignedInt(editedload[(infobyte + 5)]) + (Byte.toUnsignedInt(editedload[(infobyte + 6)]) << 8);
                                        for (n = 0; n <= (stop - start); n++) {
                                            if ((n + start) == DNP3tech.getindex() && DNP3tech.getObjGroup() == 0x1e) {
                                                output = (float) (Byte.toUnsignedInt(editedload[(infobyte + 7 + 3 * n)]) + (Byte.toUnsignedInt(editedload[(infobyte + 8 + 3 * n)]) << 8));
                                                tech_map.put(DNP3tech.getTag(), output);
                                            }
                                        }
                                        infobyte = infobyte + 6 + (stop - start + 1) * 3;
                                    }
                                }
                            }
                            infobyte = infobyte + 1;
                        } while (infobyte < j);
                    }
                }
            }
            else{
                if (Byte.toUnsignedInt(editedload[12])==0x81){
                    if(j>=22){
                        infobyte=15;
                        do{
                            if(editedload[(infobyte)]==0x01&&editedload[(infobyte+1)]==0x01){
                                start=Byte.toUnsignedInt(editedload[(infobyte+3)]) + (Byte.toUnsignedInt(editedload[(infobyte+4)]) << 8);
                                stop=Byte.toUnsignedInt(editedload[(infobyte+5)]) + (Byte.toUnsignedInt(editedload[(infobyte+6)]) << 8);
                                bytes=(stop-start+1)/8;
                                if((stop-start+1)%8>0){
                                    bytes=bytes+1;
                                }
                                for(n=0;n<bytes;n++){
                                    for(m=7;m>=0;m--){
                                        x=8*n+7-m;
                                        if((8*n+7-m)==DNP3tech.getindex()&&DNP3tech.getObjGroup()==0x01){
                                            output=(editedload[(infobyte+7+n)]>>(7-m))&0x01;
                                            tech_map.put(DNP3tech.getTag(), output);
                                        }
                                    }
                                }
                                infobyte=infobyte+6+bytes;
                            }
                            infobyte=infobyte+1;
                        }while(infobyte<j);
                    }
                }
            }
        }
        return tech_map;
    }

    //**
    // * 解析入口方法
  //   * @param DNP3Config 自定义的DNP3协议通用配置
  //   * @param globalMap 全局的参数map，key是参数名（字符串），value是参数值（浮点数）
   //  * @param payload【原始数据/tcp payload】，字节数组
   //  * @param objects 其他数据（不需要用到，写着即可）
   //  * @return 返回一个形式为key-value对的map即可
   //  */
    @Override
    public Map<String, Float> decode(DNP3Config dnp3Config, Map<String, Float> globalMap, byte[] payload, Object... obj) {
        return decode_tech(dnp3Config,globalMap,payload);
    }

    //显示协议名称
    @Override
    public String protocol() {
        return "DNP3.0";
    }
}