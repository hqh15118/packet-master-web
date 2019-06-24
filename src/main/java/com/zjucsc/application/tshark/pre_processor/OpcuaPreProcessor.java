package com.zjucsc.application.tshark.pre_processor;

import com.zjucsc.application.config.PACKET_PROTOCOL;
import com.zjucsc.tshark.packets.OpcUaPacket;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.SinglePreProcessor;

import java.util.ArrayList;
import java.util.List;

public class OpcuaPreProcessor extends SinglePreProcessor<OpcUaPacket> {
    @Override
    public FvDimensionLayer decode(OpcUaPacket packetInstance) {
        return packetInstance.layers.setFrameProtocols(PACKET_PROTOCOL.OPCA_UA);
    }

    @Override
    public Class<OpcUaPacket> decodeType() {
        return OpcUaPacket.class;
    }

    @Override
    public String singleProtocolFilterField() {
        return "opcua";
    }

    @Override
    public String extConfig() {
        return "-d tcp.port==4862,opcua";
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
            }
        };
    }

    @Override
    public String pcapPath() {
        return "C:\\Users\\Administrator\\Desktop\\xieyibaowen\\opcua\\010.pcapng";
    }
}
