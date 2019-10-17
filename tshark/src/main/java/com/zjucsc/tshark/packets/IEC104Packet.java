package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 21:16
 */
/**
 * I-格式帧：若104apci.type==0x00，则根据104asdu.typeid来判断功能码	【该格式报文为apci+asdu】
 * S-格式帧：若104apci.type==0x01，则功能码为“确认数据接收”		【该格式报文为apci】
 * U-格式帧：若104apci.type==0x03，则根据104apci.utype来判断功能码		【该格式报文为apci】
 * 【104apci.utype】	【功能码】
 * 0x01			开启命令
 * 0x02			开启确认
 * 0x04			停止命令
 * 0x08			停止确认
 * 0x10			测试命令
 * 0x20			测试确认
 */
public class IEC104Packet {

    @JSONField(name = "layers")
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer {
        @JSONField(name = "104asdu_typeid")
        //asdu
        public String[] iec104asdu_typeid={""};
        @JSONField(name = "104apci_type")
        public String[] iec104_type={"-1"};
        @JSONField(name = "104apci_utype")
        public String[] iec104apci_utype={""};
        public String iecProtocol;
    }
}
