package com.zjucsc.attack.iec104;

import com.zjucsc.attack.base.AbstractOptCommandAttackEntry;
import com.zjucsc.attack.opname.IEC104OpNameByTshark;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.IEC104Packet;

public class IECOpDecodeByTshark extends AbstractOptCommandAttackEntry<IEC104OpNameByTshark> {

    @Override
    public Object analyze(FvDimensionLayer layer, IEC104OpNameByTshark opNameConfig, Object... objs) {
        if (layer instanceof IEC104Packet.LayersBean){
            doAnalyze(((IEC104Packet.LayersBean) layer),opNameConfig);
        }
        return null;
    }

    private static StringBuilder sb = new StringBuilder();

    private void doAnalyze(IEC104Packet.LayersBean iec104Packet,IEC104OpNameByTshark opNameConfig) {
        //104asdu.causetx
        String[] causeX = iec104Packet.causetx;
        if (causeX!=null){
            String[] typeId = iec104Packet.iec104asdu_typeid;
            String ioa = iec104Packet.ioaAddress[0];
            if (typeId!=null && typeId[0].equals("45") && ioa.equals(opNameConfig.getIoaAddress())){
                int onOrOff = Integer.decode(iec104Packet.SCO[0]) & 0x1;
                sb.delete(0,sb.length());
                commandCallback(sb.append(opNameConfig.getOpName()).
                        append("[IOA]").append(String.valueOf(onOrOff == 0?"OFF":"ON"))
                        .toString(),iec104Packet);
            }
        }
    }
}
