package com.zjucsc.application.tshark.pre_processor;

import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 17:40
 */
public interface PreProcessor<P>{

    void execCommand(int type , int limit , String captureDeviceName);

    String tsharkPath();

    void pcapFilePath(int limit);

    void stopProcess();
    /**
     * 只捕获该协议的报文
     * @return
     */
    String protocolFilterField();
    /**
     * 解析的结果类型
     * @return
     */
    Class<P> decodeType();
    /**
     * 除了五元组之外，该报文需要过滤出来的字段，不可以为null，可以为空 ""
     * @return
     */
    List<String> filterFields();

}
