package com.zjucsc.application.tshark.pre_processor;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.tshark.domain.packet.FvDimensionLayer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-17 - 22:58
 */
public abstract class SinglePreProcessor<P> extends BasePreProcessor{

    @Override
    public void decodeJSONString(String packetJSON) {
        decodeThreadPool.execute(() -> {
            pipeLine.pushDataAtHead(decode(JSON.parseObject(packetJSON,decodeType())));
        });
    }

    public abstract FvDimensionLayer decode(P packetInstance);

    /**
     * 解析的结果类型
     * @return
     */
    public abstract Class<P> decodeType();

    @Override
    public String[] protocolFilterField() {
        return new String[]{singleProtocolFilterField()};
    }

    public abstract String singleProtocolFilterField();
}