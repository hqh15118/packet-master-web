package com.zjucsc.application.domain.analyzer;

import com.zjucsc.application.domain.filter.EmptyFilter;
import com.zjucsc.application.domain.filter.OtherPacketFilter;
import com.zjucsc.application.util.AbstractAnalyzer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-05 - 22:00
 */
public class EmptyPacketAnalyzer extends AbstractAnalyzer<EmptyFilter> {

    private String filterName;

    public EmptyPacketAnalyzer(EmptyFilter emptyFilter , String name) {
        super(emptyFilter);
        this.filterName = name;
    }

    @Override
    public Object analyze(Object... objs) {
        return null;
    }
}
