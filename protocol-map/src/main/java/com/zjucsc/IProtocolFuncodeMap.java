package com.zjucsc;

import java.util.Map;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-29 - 23:00
 */
public interface IProtocolFuncodeMap {
    /**
     * 该分析器分析的协议
     * @return 协议名 如s7、modbus等
     */
    String protocolAnalyzerName();

    Map<String,String> initProtocol();
}
