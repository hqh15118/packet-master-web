package com.zjucsc.attack.modbus;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.config.ModbusOptConfig;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class main {
    private static List<String > explist = new ArrayList<String>()
    {
        {
//            add("水位");
//            add(">");
//            add("5");
//            add("&&");
            add("开关");
            add("=");
            add("1");
        }
    };
    private static Map<String,Float> techmap = new HashMap<String, Float>()
    {
        {
            put("水位",10.0f);
            put("开关",1f);
        }
    };

    private static ModbusOptConfig modbusOptConfig = new ModbusOptConfig()
    {
        {
            setAddress(300);
            setBitoffset(0);
            setComment("!!!");
            setReg(1);
            setResult(true);
            setExpression(explist);
            setOpname("点亮灯泡");
        }
    };

    private static String[] payload = {"394800000033ff1700c80014012c00142800010000000000000000000000000000000000000000" +
            "000000000000000000000000000000000000"};
    private static FvDimensionLayer layer = new FvDimensionLayer()
    {
        {
            setFrame_protocols(new String[]{"modbus"});
            setTcp_payload(payload);
            setRawData(ByteUtil.hexStringToByteArray(payload[0]));
        }
    };
    private static ModbusOptAnalyzer modbusOptAnalyzer = new ModbusOptAnalyzer();
    public static void main(String[] args) {

        }
}
