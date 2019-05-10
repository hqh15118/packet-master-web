package com.zjucsc.packetmasterweb.tshark_test;

import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.util.AbstractAnalyzer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 21:37
 */
public class DefaultAnalyzer extends AbstractAnalyzer<OperationPacketFilter<String,String>> {

    public DefaultAnalyzer(OperationPacketFilter<String,String> stringOperationPacketFilter) {
        super(stringOperationPacketFilter);
    }

    @Override
    public Object analyze(Object... objs) {
        System.out.println("analyze");
        return "OK";
    }
}
