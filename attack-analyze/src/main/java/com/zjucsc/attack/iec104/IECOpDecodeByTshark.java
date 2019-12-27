package com.zjucsc.attack.iec104;

import com.zjucsc.attack.base.AbstractOptCommandAttackEntry;
import com.zjucsc.attack.opname.IEC104OpNameByTshark;
import com.zjucsc.tshark.packets.FvDimensionLayer;
import com.zjucsc.tshark.packets.IEC104Packet;

public class IECOpDecodeByTshark extends AbstractOptCommandAttackEntry<IEC104OpNameByTshark> {

    private String typeId;
    private String ioa;
    private int onOrOff = -1;
    private String optType; //select or execute

    @Override
    public Object analyze(FvDimensionLayer layer, IEC104OpNameByTshark opNameConfig, Object... objs) {
        if (layer instanceof IEC104Packet.LayersBean){
            int index = ((Integer) objs[0]);
            if (index == 0) {
                doAnalyze(((IEC104Packet.LayersBean) layer), opNameConfig);
            }else{
                if ("45".equals(typeId) && opNameConfig.getIoaAddress().equals(ioa)){
                    sb.delete(0,sb.length());
                    commandCallback(sb.append(opNameConfig.getOpName()).
                            append("[IOA]").append(ioa).append(String.valueOf(onOrOff == 0?"OFF":"ON"))
                            .toString(),layer);
                }
            }
        }
        return null;
    }

    private static StringBuilder sb = new StringBuilder();

    private void doAnalyze(IEC104Packet.LayersBean iec104Packet,IEC104OpNameByTshark opNameConfig) {
        //104asdu.causetx
        String[] causeX = iec104Packet.causetx;
        if (causeX!=null && (causeX[0].equals("6") || causeX[0].equals("7"))){//6 is "Act"
            typeId = iec104Packet.iec104asdu_typeid[0];
            ioa = iec104Packet.ioaAddress[0];
            if (typeId.equals("45") && ioa.equals(opNameConfig.getIoaAddress())){
                int SCOInt = Integer.decode(iec104Packet.SCO[0]);
                onOrOff = SCOInt & 0x1;
                optType = (SCOInt & 0x80) != 0 ? "SELECT":"EXECUTE";
                sb.delete(0,sb.length());
                commandCallback(sb.append(opNameConfig.getOpName()).
                        append("[IOA]").append(ioa).append(">").
                        append(optType).append(" ").append(String.valueOf(onOrOff == 0?"OFF":"ON"))
                        .toString(),iec104Packet);
            }
        }
    }

    @Override
    public void afterRoundDecode() {
        this.typeId = null;
        this.ioa = null;
        this.onOrOff = -1;
        this.optType = null;
    }
}
