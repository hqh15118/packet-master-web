package com.zjucsc.application.domain.analyzer;

import com.zjucsc.application.domain.filter.TcpPacketFilter;
import com.zjucsc.application.util.AbstractAnalyzer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 20:41
 */
public class TcpAnalyzer extends AbstractAnalyzer<TcpPacketFilter> {
    public TcpAnalyzer(TcpPacketFilter o) {
        super(o);
    }

    @Override
    public Object analyze(Object... objs) {

        return null;
    }
}
