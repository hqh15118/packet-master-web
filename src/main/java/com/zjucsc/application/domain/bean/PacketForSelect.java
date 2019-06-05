package com.zjucsc.application.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PacketForSelect implements Serializable {

    /**
     * list : [{"timeStamp":" 1","protocolName":"1 ","srcMac":"1 ","dstMac":"1 ","srcIp":"1 ","dstIp":"1 ","srcPort":" 1","dstPort":"1 ","funcode":"1 ","length":"1 "}]
     * count : 100
     */

    private int count;
    @JSONField(name = "list")
    private List<SavedPacket> savedPackets;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SavedPacket> getSavedPackets() {
        return savedPackets;
    }

    public void setSavedPackets(List<SavedPacket> savedPackets) {
        this.savedPackets = savedPackets;
    }
}
