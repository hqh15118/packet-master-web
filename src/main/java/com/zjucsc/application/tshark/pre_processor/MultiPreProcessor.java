package com.zjucsc.application.tshark.pre_processor;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.tshark.domain.bean.SingleProtocol;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-17 - 23:06
 */

@Slf4j
public abstract class MultiPreProcessor extends BasePreProcessor {

    private String[] packetProtocolStacks;

    public MultiPreProcessor(){
        this.packetProtocolStacks = packetProtocolStacks();
    }

    @Override
    public void decodeJSONString(String packetJSON) {
        SingleProtocol singleProtocol = JSON.parseObject(packetJSON, SingleProtocol.class);
        int i = 0;
        String protocolStack = singleProtocol.layers.frame_protocols[0];
        for (String packetProtocolStack : packetProtocolStacks) {
            if (protocolStack.endsWith(packetProtocolStack)){
                final int j = i;
                decodeThreadPool.execute(()->{
                    pipeLine.pushDataAtHead(outPacketIndexInStacks(j,packetJSON));
                });
                break;
            }
            i++;
        }
        if (i == packetProtocolStacks.length){
            log.error("报文解析协议栈匹配失败：{} -- {} " , protocolStack , packetProtocolStacks);
        }
    }

    /**
     * e.g. "eth:ethertype:ip:tcp:tpkt:cotp:s7comm" --> S7Packet.class
     * @return protocol_stack -> packet class
     */
    public abstract String[] packetProtocolStacks();

    public abstract FvDimensionLayer outPacketIndexInStacks(int index , String packetJSON);
}
