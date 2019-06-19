package com.zjucsc.application.tshark.domain.packet;

import com.alibaba.fastjson.annotation.JSONField;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.Data;


@Data
public class OpcUaPacket {
    /**
     * timestamp : 1560149163130
     * layers : {"opcua_ClientHandle":["38","39"],"opcua_datavalue_has_value":["1","1"],"opcua_Boolean":["1","1"]}
     *
     *
     * opcua.transport.type //OPC UA的报文类型标记
     * opcua.servicenodeid.numeric //服务编码，用于区分报文的具体操作
     * opcua.RequestHandle //用于标记报文中的request和response的对应关系
     * opcua.nodeid.string //创建监控变量需要关注的字段，保存了变量的名字
     * opcua.MonitoredItemId //创建监控变量需要关注的字段，代表变量名字所对应的编号（之一）
     * opcua.MonitoredItemIds //与上一条类似，是MonitoredItemIds形成的数组
     * opcua.ClientHandle //变量所对应的编号
     * opcua.variant.has_value //标记变量的类型
     *
     * //以下为变量的值
     * opcua.Boolean //布尔量
     * opcua.SByte //单字节有符号整数
     * opcua.Byte //单字节无符号整数
     * opcua.Int16
     * opcua.UInt16
     * opcua.Int32
     * opcua.UInt32
     * opcua.Int64
     * opcua.UInt64
     * opcua.Float //单精度浮点数
     * opcua.Double //双精度浮点数
     */
    public LayersBean layers;

    public static class LayersBean extends FvDimensionLayer  {
        @JSONField(name = "opcua_transport_type")
        public String[] opcua_transport_type;
        @JSONField(name = "opcua_servicenodeid_numeric")
        public String[] opcua_servicenodeid_numeric;
        @JSONField(name = "opcua_RequestHandle")
        public String[] opcua_request_handle;
        @JSONField(name = "opcua_nodeid_string")
        public String[] opcua_nodeid_string;
        @JSONField(name = "opcua_MonitoredItemId")
        public String[] opcua_monitoreditemid;
        @JSONField(name = "opcua_MonitoredItemIds")
        public String[] opcua_monitoreditemids;
        @JSONField(name = "opcua_ClientHandle")
        public String[] opcua_clienthandle;
        @JSONField(name = "opcua_variant_has_value")
        public String[] opcua_variant_has_value;
        @JSONField(name = "opcua_Boolean")
        public String[] opcua_boolean;
        @JSONField(name = "opcua_SByte")
        public String[] opcua_sbyte;
        @JSONField(name = "opcua_Byte")
        public String[] opcua_byte;
        @JSONField(name = "opcua_Int16")
        public String[] opcua_int16;
        @JSONField(name = "opcua_Int32")
        public String[] opcua_int32;
        @JSONField(name = "opcua_UInt32")
        public String[] opcua_uint32;
        @JSONField(name = "opcua_Int64")
        public String[] opcua_uint64;
        @JSONField(name = "opcua_Float")
        public String[] opcua_float;
        @JSONField(name = "opcua_Double")
        public String[] opcua_double;
    }
}
