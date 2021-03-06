package com.zjucsc.tshark.pre_processor;

import com.alibaba.fastjson.JSON;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-17 - 22:58
 */
public abstract class SinglePreProcessor<P> extends BasePreProcessor{

    @Override
    public void decodeJSONString(String packetJSON) {
//        decodeThreadPool.execute(() -> {
//            //将数据推入到pipeLine中
//            pipeLine.pushDataAtHead(postDecode(decode(JSON.parseObject(preDecode(packetJSON),decodeType()))));
//        });
        pipeLine.pushDataAtHead(postDecode(decode(JSON.parseObject(preDecode(packetJSON),decodeType()))));
    }

    public abstract FvDimensionLayer decode(P packetInstance);


    public String preDecode(String jsonData){
        return jsonData;
    }

    public FvDimensionLayer postDecode(FvDimensionLayer layer){
        return layer;
    }
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
