package com.zjucsc.application.tshark.pre_processor;

import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-11 - 17:40
 */
public interface PreProcessor<P>{

    void execCommand();

    String tsharkPath();

    void pcapFilePath(int limit);

    void stopProcess();

    String protocolFilterField();

    Class<P> decodeType();

    List<String> filterFields();
}
