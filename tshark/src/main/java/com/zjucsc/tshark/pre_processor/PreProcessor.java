package com.zjucsc.tshark.pre_processor;

import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 17:40
 */
public interface PreProcessor {

    void execCommand(int type, int limit);

    String tsharkPath();

    void pcapFilePath(int limit);

    void stopProcess();
    /**
     * 只捕获该协议的报文
     * @return
     */
    String[] protocolFilterField();

    /**
     * 除了五元组之外，该报文需要过滤出来的字段，不可以为null，可以为空 ""
     * @return
     */
    List<String> filterFields();

    //-f "not ether src 28:d2:44:5f:69:e1 and tcp"
    String filter();
}
