package com.zjucsc.application.tshark.analyzer;

import com.zjucsc.application.config.DangerLevel;
import com.zjucsc.application.tshark.domain.BadPacket;
import com.zjucsc.application.tshark.filter.OperationPacketFilter;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.common.exceptions.ProtocolIdNotValidException;
import com.zjucsc.tshark.analyzer.AbstractAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.extern.slf4j.Slf4j;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 21:49
 */

@Slf4j
//OperationPacketFilter<Integer,String>   --->    <fun_code , fun_code_meaning>
public class OperationAnalyzer extends AbstractAnalyzer<OperationPacketFilter<Integer,String>> {
    @Override
    public Object analyze(Object... objs){
        int fun_code = ((int) objs[0]);
        FvDimensionLayer layer = ((FvDimensionLayer) objs[1]);
        if (!getAnalyzer().getWhiteMap().containsKey(fun_code)){
            try {
                return new BadPacket.Builder(layer.frame_protocols[0])
                        .setComment("不允许操作")
                        .set_five_Dimension(layer)
                        .setDangerLevel(DangerLevel.VERY_DANGER)
                        .setFun_code(fun_code)
                        .setOperation(getOperation(layer.frame_protocols[0],fun_code))
                        .build();
            } catch (ProtocolIdNotValidException e) {
                log.error("protocol <==> ID not valid " , e);
            }
        }
        return null;
    }

    public OperationAnalyzer(OperationPacketFilter<Integer, String> integerStringPacketFilter) {
        super(integerStringPacketFilter);
    }

    private String getOperation(String protocol , int fun_code) throws ProtocolIdNotValidException {
        String str = CommonConfigUtil.getTargetProtocolFuncodeMeanning(protocol,fun_code);
        return str==null ? "unknown operation" : str;
    }

    @Override
    public String toString() {
        return getAnalyzer().toString();
    }
}
