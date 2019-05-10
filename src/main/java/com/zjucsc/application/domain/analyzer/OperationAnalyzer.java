package com.zjucsc.application.domain.analyzer;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.config.DangerLevel;
import com.zjucsc.application.domain.bean.BadPacket;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.domain.filter.OperationPacketFilter;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.util.AbstractAnalyzer;
import com.zjucsc.application.util.CommonConfigUtil;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 21:49
 */

//OperationPacketFilter<Integer,String> <fun_code , fun_code_meaning>
public class OperationAnalyzer extends AbstractAnalyzer<OperationPacketFilter<Integer,String>> {
    @Override
    public Object analyze(Object... objs) throws ProtocolIdNotValidException {
        int fun_code = ((int) objs[0]);
        FiveDimensionPacketWrapper wrapper = ((FiveDimensionPacketWrapper) objs[1]);
        if (getAnalyzer().getBlackMap().containsKey(fun_code)){
            return new BadPacket.Builder(wrapper.fiveDimensionPacket.protocol)
                    .setComment("黑名单操作")
                    .set_five_Dimension(wrapper.fiveDimensionPacket)
                    .setDangerLevel(DangerLevel.VERY_DANGER)
                    .setFun_code(fun_code)
                    .setOperation(getOperation(wrapper.fiveDimensionPacket.protocol,fun_code))
                    .build();
        }
        return null;
    }

    public OperationAnalyzer(OperationPacketFilter<Integer, String> integerStringPacketFilter) {
        super(integerStringPacketFilter);
    }

    private String getOperation(String protocol , int fun_code) throws ProtocolIdNotValidException {
        String str = CommonConfigUtil.getTargetProtocolFuncodeMeanning(protocol,fun_code);
        return str==null?"unknown operation" : str;
    }

    @Override
    public String toString() {
        return getAnalyzer().toString();
    }
}
