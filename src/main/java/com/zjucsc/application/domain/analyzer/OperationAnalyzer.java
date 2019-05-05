package com.zjucsc.application.domain.analyzer;

import com.zjucsc.application.config.DangerLevel;
import com.zjucsc.application.domain.bean.BadPacket;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.util.AbstractAnalyzer;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 21:49
 */
public class OperationAnalyzer extends AbstractAnalyzer<OperationPacketFilter<Integer,String>> {
    @Override
    public Object analyze(Object... objs) {
        int fun_code = ((int) objs[0]);
        FiveDimensionPacketWrapper wrapper = ((FiveDimensionPacketWrapper) objs[1]);
        if (getAnalyzer().getBlackMap().containsKey(fun_code)){
            return new BadPacket.Builder(wrapper.fiveDimensionPacket.protocol)
                    .setComment("黑名单操作")
                    .set_five_Dimension(wrapper.fiveDimensionPacket)
                    .setDangerLevel(DangerLevel.VERY_DANGER)
                    .setFun_code(fun_code)
                    //.setOperation(Common.CONFIGURATION_MAP.get(wrapper.fiveDimensionPacket.protocol)
                    //.set(fun_code))
                    .setOperation("not support")
                    .build();
        }
//        if (!getAnalyzer().getWhiteMap().containsKey(fun_code)){
//            return new BadPacket.Builder(wrapper.fiveDimensionPacket.protocol)
//                    .setComment("白名单未指定操作")
//                    .set_five_Dimension(wrapper.fiveDimensionPacket)
//                    .setDangerLevel(DangerLevel.DANGER)
//                    .setFun_code(fun_code)
//                    //.setOperation(Common.CONFIGURATION_MAP.get(wrapper.fiveDimensionPacket.protocol)
//                    //.set(fun_code))
//                    .setOperation("not support")
//                    .build();
//        }
        return null;
    }

    public OperationAnalyzer(OperationPacketFilter<Integer, String> integerStringPacketFilter) {
        super(integerStringPacketFilter);
    }



}
