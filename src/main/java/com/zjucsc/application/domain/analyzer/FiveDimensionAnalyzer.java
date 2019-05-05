package com.zjucsc.application.domain.analyzer;

import com.zjucsc.application.domain.bean.BadPacket;
import com.zjucsc.application.domain.filter.FiveDimensionPacketFilter;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.tshark.handler.BadPacketAnalyzeHandler;
import com.zjucsc.application.util.AbstractAnalyzer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 21:45
 * be used in BadPacketAnalyzeHandler
 * @see BadPacketAnalyzeHandler
 */

/*
 * 五元组分析器
 */
public class FiveDimensionAnalyzer extends AbstractAnalyzer<FiveDimensionPacketFilter> {


    public FiveDimensionAnalyzer(FiveDimensionPacketFilter fiveDimensionPacketFilter) {
        super(fiveDimensionPacketFilter);
    }

    @Override
    public Object analyze(Object... objs) {
        FiveDimensionPacketWrapper wrapper = ((FiveDimensionPacketWrapper) objs[0]);
        BadPacket badPacket = getAnalyzer().ERROR(wrapper);
        if (badPacket != null){
            //黑名单匹配
            return badPacket;
        }
        //黑名单匹配失败后白名单匹配
        //这部分如果返回null，要么是白名单匹配成功，要么是白名单没有指定任何的规则
        return getAnalyzer().OK(wrapper);
    }

}
