package com.zjucsc.application.tshark.pre_processor;

import com.alibaba.fastjson.JSON;
import com.zjucsc.tshark.packets.CipPacket;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.MmsPacket;
import com.zjucsc.tshark.pre_processor.MultiPreProcessor;

import java.util.ArrayList;
import java.util.List;

public class CipMmsPreProcessor extends MultiPreProcessor {
    @Override
    public String[] packetProtocolStacks() {
        return new String[]{"cip","mms"};
    }

    @Override
    public FvDimensionLayer outPacketIndexInStacks(int index, String packetJSON) {
        switch (index){
            case 0 :
                CipPacket cipPacket = JSON.parseObject(packetJSON,CipPacket.class);
                return cipPacket.layers;
            case 1 :
                MmsPacket mmsPacket = JSON.parseObject(packetJSON,MmsPacket.class);
                return mmsPacket.layers;
        }
        return null;
    }

    @Override
    public String[] protocolFilterField() {
        return new String[]{"cip","mms"};
    }

    @Override
    public List<String> filterFields() {
        return new ArrayList<String>(){
            {
                add("cip.sc");
                add("mms.confirmedServiceRequest");
            }
        };
    }
}
