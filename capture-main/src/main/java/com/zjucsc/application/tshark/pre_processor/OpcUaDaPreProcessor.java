package com.zjucsc.application.tshark.pre_processor;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.util.CacheUtil;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.application.util.ConfigUtil;
import com.zjucsc.tshark.packets.*;
import com.zjucsc.tshark.pre_processor.MultiPreProcessor;

import java.util.ArrayList;
import java.util.List;

public class OpcUaDaPreProcessor extends MultiPreProcessor {
    @Override
    public String[] packetProtocolStacks() {
        return new String[]{"opcua","dcerpc"};
    }

    @Override
    public FvDimensionLayer outPacketIndexInStacks(int index, String packetJSON) {
        switch (index){
            case 0 :
                OpcUaPacket opcUaPacket = JSON.parseObject(packetJSON, OpcUaPacket.class);
                return opcUaPacket.layers;
            case 1 :
                OpcDaPacket opcDaPacket = JSON.parseObject(packetJSON, OpcDaPacket.class);
                return opcDaPacket.layers;
        }
        return null;
    }

    @Override
    public String[] protocolFilterField() {
        return new String[]{"opcua","dcerpc"};
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("opcua.transport.type");
                add("opcua.servicenodeid.numeric");
                add("opcua.RequestHandle");
                add("opcua.nodeid.string");
                add("opcua.MonitoredItemId");
                add("opcua.MonitoredItemIds");
                add("opcua.ClientHandle");
                add("opcua.variant.has_value");
                add("opcua.Boolean");
                add("opcua.SByte");
                add("opcua.Byte");
                add("opcua.Int16");
                add("opcua.UInt16");
                add("opcua.Int32 ");
                add("opcua.UInt32");
                add("opcua.Int64");
                add("opcua.UInt64");
                add("opcua.Float");
                add("opcua.Double");
                add("opcua.datavalue.mask");
                add("opcua.StatusCode");
                add("dcerpc.pkt_type");
                add("dcerpc.datype");
                add("dcerpc.stub_data");
                add("dcerpc.cn_call_id");
            }
        };
    }

    @Override
    public String extConfig() {
        return "-d tcp.port==4862,opcua";
    }
}
