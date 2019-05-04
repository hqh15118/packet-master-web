package com.zjucsc.application.tshark.analyzer;

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

    public OtherPacketAnalyzer(OtherPacketFilter otherPacketFilter) {
        super(otherPacketFilter);
    }

    @Override
    public Object analyze(Object... objs) {
        log.error("not define protocol {} 's analyzer" , (String)objs[1]);
        return null;
    }
}
