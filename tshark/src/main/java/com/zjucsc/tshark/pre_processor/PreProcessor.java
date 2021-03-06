package com.zjucsc.tshark.pre_processor;

import com.zjucsc.tshark.handler.PipeLine;

import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 17:40
 */
public interface PreProcessor extends TsharkPreProcessor{

    void pcapFilePath(int limit);

    /**
     * 只使用该协议的报文 -Y
     * @return
     */
    String[] protocolFilterField();

    /**
     * 除了五元组之外，该报文需要过滤出来的字段，不可以为null，可以为空 ""
     * -e tcp.port ...
     * @return
     */
    List<String> filterFields();

    //-f "not ether src 28:d2:44:5f:69:e1 and tcp"
    String filter();

    void decodeJSONString(String packetJSON);

}
