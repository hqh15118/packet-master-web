package com.zjucsc.application.tshark.analyzer;

import com.zjucsc.application.tshark.filter.OperationPacketFilter;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.common.AttackTypePro;
import com.zjucsc.tshark.analyzer.AbstractAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import lombok.extern.slf4j.Slf4j;

import static com.zjucsc.application.util.PacketDecodeUtil.getAttackBeanInfo;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 21:49
 */

@Slf4j
//OperationPacketFilter<Integer,String>   --->    <fun_code , fun_code_meaning>
public class OperationAnalyzer extends AbstractAnalyzer<OperationPacketFilter<String,String>> {
    @Override
    public Object analyze(Object... objs){
        String fun_code = ((String) objs[0]);
        FvDimensionLayer layer = ((FvDimensionLayer) objs[1]);
        String protocol = (String)objs[2];
        if (!getAnalyzer().getWhiteMap().containsKey(fun_code)){
            return new AttackBean.Builder()
                    .attackInfo(getAttackBeanInfo(protocol,fun_code))
                    .attackType(AttackTypePro.VISIT_COMMAND)
                    .fvDimension(layer)
                    .build();
        }
        return null;
    }


    public OperationAnalyzer(OperationPacketFilter<String, String> integerStringPacketFilter) {
        super(integerStringPacketFilter);
    }

    @Override
    public String toString() {
        return getAnalyzer().toString();
    }
}
