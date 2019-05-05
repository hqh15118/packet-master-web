package com.zjucsc.application.domain.analyzer;

import com.zjucsc.application.domain.filter.OtherPacketFilter;
import com.zjucsc.application.util.AbstractAnalyzer;
import lombok.extern.slf4j.Slf4j;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-04 - 20:45
 */
@Slf4j
public class OtherPacketAnalyzer extends AbstractAnalyzer<OtherPacketFilter> {

    private String analyzerName;

    public OtherPacketAnalyzer(OtherPacketFilter otherPacketFilter) {
        super(otherPacketFilter);
    }

    public OtherPacketAnalyzer(OtherPacketFilter otherPacketFilter , String name) {
        super(otherPacketFilter);
        this.analyzerName = name;
    }

    @Override
    public Object analyze(Object... objs) {
        return null;
    }
}
