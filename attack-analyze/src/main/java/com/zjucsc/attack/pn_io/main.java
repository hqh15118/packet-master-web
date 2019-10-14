package com.zjucsc.attack.pn_io;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.config.PnioOptConfig;
import com.zjucsc.common.util.ByteUtil;
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
    private static PnioOptConfig pnioOptConfig = new PnioOptConfig(){
        {
            setResult(true);
            setMacaddress("e0:dc:a0:7b:16:56");
            setExpression(explist);
            setComment("!!!");
            setByteoffset(0);
            setBitoffset(0);
            setOpname("启动DO1");
        }
    };

    private static PnioOptDecode pnioOptDecode = new PnioOptDecode();

    private static byte[] load = ByteUtil.hexStringToByteArray("e0dca07bfc3be0dca07b16568892800001000000000080808080" +
            "0000000000000000000000000000000000000000000000000000000000008d00350000140d07fc6f84da200b2020fc6f84da200b20900000005f");

    private static FvDimensionLayer layer = new FvDimensionLayer()
    {
        {
            setRawData(load);
            setEth_src(new String[] {"e0:dc:a0:7b:16:56"});
        }
    };

    public static void main(String[] args) {
        AttackBean attackBean = pnioOptDecode.doAnalyze(layer, techmap, pnioOptConfig, load);
        if(attackBean!=null) {
            System.out.println(attackBean.getAttackType());
            System.out.println(attackBean.getAttackInfo());
        }
    }
}
