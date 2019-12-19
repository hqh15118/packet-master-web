package com.zjucsc.application.tshark.pre_processor;

import com.alibaba.fastjson.JSON;
import com.zjucsc.tshark.packets.Dnp3_0Packet;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.IEC104Packet;
import com.zjucsc.tshark.pre_processor.MultiPreProcessor;

import java.util.ArrayList;
import java.util.List;

public class IEC104DnpPreProcessor extends MultiPreProcessor {
    @Override
    public String[] packetProtocolStacks() {
        return new String[]{"104apci","104asdu","dnp3"};
    }

    @Override
    public FvDimensionLayer outPacketIndexInStacks(int index, String packetJSON) {
        switch (index){
            case 0 :
            case 1:
                IEC104Packet iec104Packet = JSON.parseObject(packetJSON,IEC104Packet.class);
                return iec104Packet.layers;
            case 2 :
                Dnp3_0Packet dnp3_0Packet = JSON.parseObject(packetJSON,Dnp3_0Packet.class);
                return dnp3_0Packet.layers;
        }
        return null;
    }

    @Override
    public String[] protocolFilterField() {
        return new String[]{"104apci or 104asdu","dnp3"};
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("dnp3.ctl.prm");
                add("dnp3.ctl.prifunc");
                add("dnp3.ctl.secfunc");
                add("dnp3.al.boq.b7");
                add("dnp3.al.boq.b0");
                add("dnp3.al.ana.int");
                add("dnp3.al.point_index");
                add("dnp3.al.obj");
                add("dnp3.al.biq.b7");
                add("dnp3.al.range.start");
                add("dnp3.al.range.stop");

                add("104asdu.typeid");
                add("104apci.type");
                add("104apci.utype");
                add("104asdu.ioa");
                add("104asdu.float");
                add("104asdu.siq");
                add("104asdu.sco");
                add("104asdu.normval");
                add("104asdu.scalval");
                add("104asdu.numix");

            }
        };
    }
}
