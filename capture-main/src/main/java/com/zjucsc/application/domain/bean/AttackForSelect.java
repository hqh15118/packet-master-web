package com.zjucsc.application.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class AttackForSelect extends BaseResponse implements Serializable {

    /**
     * list : [{"timeStamp":" 1","protocolName":"1 ","srcMac":"1 ","dstMac":"1 ","srcIp":"1 ","dstIp":"1 ","srcPort":" 1","dstPort":"1 ","funcode":"1 ","length":"1 "}]
     * count : 100
     */

    private int count;
    @JSONField(name = "list")
    private List<SavedAttackPacket> savedPackets;

}
