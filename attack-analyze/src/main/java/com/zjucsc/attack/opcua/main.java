package com.zjucsc.attack.opcua;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.OpcUaPacket;

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

    private static OpcuaOptAnalyzer opcuaOptAnalyzer= new OpcuaOptAnalyzer();

    private static OpcuaOptConfig opcuaOptConfig = new OpcuaOptConfig()
    {
        {
            setComment("!!!");
            setName("t|LOCK01.Man_Close");
            setResult(1f);
            setExpression(explist);
            setOpname("aaa");
        }
    };
    private static OpcUaPacket.LayersBean layer = new OpcUaPacket.LayersBean(){
        {
            setOpcua_datavalue_mask(new String[]{"0x01"});
            setOpcua_nodeid_string(new String[]{"t|LOCK01.Man_Close"});
            setOpcua_servicenodeid_numeric(new  String[] {"673"});
            setOpcua_variant_has_value(new String[] {"0x01"});
            setRawData(new byte[] {0x01});
        }
    };

    public static void main(String[] args) {
        AttackBean attackBean = opcuaOptAnalyzer.doAnalyze(layer, techmap, opcuaOptConfig);
        if(attackBean!=null) {
            System.out.println(attackBean.getAttackType());
            System.out.println(attackBean.getAttackInfo());
        }
    }
}
