package com.zjucsc;

import com.zjucsc.application.tshark.pre_processor.PnioPreProcessor;
import com.zjucsc.application.tshark.pre_processor.S7CommPreProcessor;
import com.zjucsc.application.util.PacketDecodeUtil;
import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.attack.pn_io.PnioOptConfig;
import com.zjucsc.attack.pn_io.PnioOptDecode;
import com.zjucsc.attack.s7comm.S7OptAnalyzer;
import com.zjucsc.attack.s7comm.S7OptAttackConfig;
import com.zjucsc.common.common_util.ByteUtil;
import com.zjucsc.tshark.TsharkCommon;
import com.zjucsc.tshark.handler.AbstractAsyncHandler;
import com.zjucsc.tshark.handler.DefaultPipeLine;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.pre_processor.BasePreProcessor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

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
    private static S7OptAttackConfig s7OptAttackConfig = new S7OptAttackConfig()
    {
        {
            setBitoffset(6);
            setByteoffset(0);
            setOpname("闸1打开");
            setComment("!!!");
            setDBnum(2);
            setResult(true);
            setExpression(explist);
        }
    };

    private static PnioOptConfig pnioOptConfig = new PnioOptConfig(){{
        setBitoffset(0);
        setByteoffset(0);
        setComment("!!!");
        setExpression(explist);
        setMacaddress(new byte[] {(byte)0xe0,(byte)0xdc,(byte)0xa0,0x7b,0x16,0x56});
        setResult(true);
    }};

    private static S7OptAnalyzer s7OptAnalyzer = new S7OptAnalyzer();
    private static PnioOptDecode pnioOptDecode = new PnioOptDecode();

    private static String[] pro = new String[] {"s7comm"};

    private static String[] payload = new String[] {"03:00:00:24:02:f0:80:32:01:00:00:24:00:00:0e:00:05:05:01:12:0a:10:01:00:01:00:02:84:00:00:06:00:03:00:01:01"};

    private static byte[] load = PacketDecodeUtil.hexStringToByteArray(payload[0]);

    private static FvDimensionLayer layer = new FvDimensionLayer()
    {
        {
            setTcp_payload(payload);
            setFrame_protocols(pro);
            setRawData(load);
        }
    };

    public static void main1(String[] args)
    {
        BasePreProcessor iec104DnpPreProcessor = new S7CommPreProcessor();
        BasePreProcessor.setCaptureDeviceNameAndMacAddress("11:22:33:44:55:66","eth0");
        DefaultPipeLine pipeLine = new DefaultPipeLine("processor name");
        TsharkCommon.setErrorCallback(new TsharkCommon.ErrorCallback() {
            @Override
            public void errorCallback(String errorMsg) {
            }
        });
        pipeLine.addLast(new AbstractAsyncHandler(Executors.newSingleThreadExecutor()) {
            @Override
            public Object handle(Object t) {
                FvDimensionLayer layer = ((FvDimensionLayer) t);
                byte[] payload = ByteUtil.hexStringToByteArray(layer.custom_ext_raw_data[0]);    //t-s
                layer.setRawData(payload);
//                System.out.println(payload);
                AttackBean attackBean = s7OptAnalyzer.doAnalyze(layer, techmap, s7OptAttackConfig, payload);
                if(attackBean!=null) {
                    System.out.println(attackBean.getAttackType());
                    System.out.println(attackBean.getAttackInfo());
                }
                return null;
            }
        });
        iec104DnpPreProcessor.setPipeLine(pipeLine);
        iec104DnpPreProcessor.execCommand(1,-1);
    }

    public static void main(String[] args) {
        AttackBean attackBean = s7OptAnalyzer.doAnalyze(layer, techmap, s7OptAttackConfig, load);
                if(attackBean!=null) {
                    System.out.println(attackBean.getAttackType());
                    System.out.println(attackBean.getAttackInfo());
                }
    }

}
