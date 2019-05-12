package com.zjucsc.packetmasterweb.tshark_test.demos.pre_processor;

import com.zjucsc.application.tshark.decode.PipeLine;

import java.io.IOException;
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
