package com.zjucsc.tshark.packets;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Arrays;


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
        @JSONField(name = "opcua_Int64")
        public String[] opcua_int64;
        @JSONField(name = "opcua_uInt16")
        public String[] opcua_uint16;
        @JSONField(name = "opcua_UInt32")
        public String[] opcua_uint32;
        @JSONField(name = "opcua_Int64")
        public String[] opcua_uint64;
        @JSONField(name = "opcua_Float")
        public String[] opcua_float;
        @JSONField(name = "opcua_Double")
        public String[] opcua_double;
        @JSONField(name = "opcua_datavalue_mask")
        public String[] opcua_datavalue_mask;
        @JSONField(name = "opcua_StatusCode")
        public String[] opcua_StatusCode;

        @Override
        public String toString() {
            return "LayersBean{" +
                    "opcua_transport_type=" + Arrays.toString(opcua_transport_type) +
                    ", opcua_servicenodeid_numeric=" + Arrays.toString(opcua_servicenodeid_numeric) +
                    ", opcua_request_handle=" + Arrays.toString(opcua_request_handle) +
                    ", opcua_nodeid_string=" + Arrays.toString(opcua_nodeid_string) +
                    ", opcua_monitoreditemid=" + Arrays.toString(opcua_monitoreditemid) +
                    ", opcua_monitoreditemids=" + Arrays.toString(opcua_monitoreditemids) +
                    ", opcua_clienthandle=" + Arrays.toString(opcua_clienthandle) +
                    ", opcua_variant_has_value=" + Arrays.toString(opcua_variant_has_value) +
                    ", opcua_boolean=" + Arrays.toString(opcua_boolean) +
                    ", opcua_sbyte=" + Arrays.toString(opcua_sbyte) +
                    ", opcua_byte=" + Arrays.toString(opcua_byte) +
                    ", opcua_int16=" + Arrays.toString(opcua_int16) +
                    ", opcua_int32=" + Arrays.toString(opcua_int32) +
                    ", opcua_int64=" + Arrays.toString(opcua_int64) +
                    ", opcua_uint16=" + Arrays.toString(opcua_uint16) +
                    ", opcua_uint32=" + Arrays.toString(opcua_uint32) +
                    ", opcua_uint64=" + Arrays.toString(opcua_uint64) +
                    ", opcua_float=" + Arrays.toString(opcua_float) +
                    ", opcua_double=" + Arrays.toString(opcua_double) +
                    ", opcua_datavalue_mask=" + Arrays.toString(opcua_datavalue_mask) +
                    ", opcua_StatusCode=" + Arrays.toString(opcua_StatusCode) +
                    ", frame_protocols=" + Arrays.toString(frame_protocols) +
                    ", eth_dst=" + Arrays.toString(eth_dst) +
                    ", frame_cap_len=" + Arrays.toString(frame_cap_len) +
                    ", eth_src=" + Arrays.toString(eth_src) +
                    ", ip_src=" + Arrays.toString(ip_src) +
                    ", ip_dst=" + Arrays.toString(ip_dst) +
                    ", src_port=" + Arrays.toString(src_port) +
                    ", dst_port=" + Arrays.toString(dst_port) +
                    '}';
        }
    }
}
