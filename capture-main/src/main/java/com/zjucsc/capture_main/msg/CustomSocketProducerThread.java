package com.zjucsc.capture_main.msg;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.capture_main.bean.MsgWrapper;
import com.zjucsc.capture_main.protobuf.ArtArgData;
import com.zjucsc.capture_main.protobuf.AttackBeanOuterClass;
import com.zjucsc.capture_main.protobuf.FvDimension;
import com.zjucsc.capture_main.protobuf.StatisticInfoOuterClass;
import com.zjucsc.common_util.ByteUtil;
import com.zjucsc.msg_socket.BaseSocketProducerThread;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Map;

import static com.zjucsc.capture_main.msg.CustomSocketConsumerThread.MSG_TYPE.*;

public class CustomSocketProducerThread extends BaseSocketProducerThread {

    private ArtArgData.ArtArg.Builder artArgOrBuilder = ArtArgData.ArtArg.newBuilder();
    private AttackBeanOuterClass.AttackBean.Builder attackBuilder = AttackBeanOuterClass.AttackBean.newBuilder();
    private StatisticInfoOuterClass.StatisticInfo.Builder statisticBuilder = StatisticInfoOuterClass.StatisticInfo.newBuilder();
    private FvDimension.FvDimensionLayer.Builder fvDimensionBuilder = FvDimension.FvDimensionLayer.newBuilder();
    private final byte[] head = new byte[]{0x7F,0x09};
    private byte msgType = 0;

    @Override
    protected void handleMsgFromQueue(BufferedOutputStream bfos, Object data) {
        if (data instanceof FvDimensionLayer){
            handleFvDimension(bfos,((FvDimensionLayer) data));
        }else if (data instanceof AttackBean){
            handleAttackBean(bfos,((AttackBean) data));
        }else if (data instanceof MsgWrapper){
            handleMsgWrapper(bfos,((MsgWrapper) data));
        }else{
            System.err.println("程序错误，无法解析数据类型 [CustomSocketProducerThread]");
        }
    }

    @SuppressWarnings("unchecked")
    private void handleMsgWrapper(BufferedOutputStream bfos,MsgWrapper data) {
        Map map = data.getMap();
        byte[] byteData;
        if (data.getType() == 0){
            //art args
            artArgOrBuilder.putAllValues(map);
            byteData = artArgOrBuilder.build().toByteArray();
            msgType = (byte) ART_ARG;
        }else{
            //statistic info
            statisticBuilder.putAllValues(map);
            byteData = statisticBuilder.build().toByteArray();
            msgType = (byte) STATISTICS_INFO;
        }
        doSend(byteData,bfos,msgType);
    }

    private void handleAttackBean(BufferedOutputStream bfos,AttackBean data) {
        attackBuilder.setAttackInfo(data.getAttackInfo());
        attackBuilder.setAttackType(data.getAttackType());
        msgType = (byte)ATTACK;
        doSend(attackBuilder.build().toByteArray(),bfos,msgType);
    }

    private void handleFvDimension(BufferedOutputStream bfos,FvDimensionLayer data) {
        fvDimensionBuilder.setCollectorId(data.collectorId);
        fvDimensionBuilder.setDelay(data.delay);
        fvDimensionBuilder.setFunCode(data.funCode);
        fvDimensionBuilder.setFunCodeMeaning(data.funCodeMeaning);
        fvDimensionBuilder.setEthSrc(data.eth_src[0]);
        fvDimensionBuilder.setEthDst(data.eth_dst[0]);
        fvDimensionBuilder.setIpSrc(data.ip_src[0]);
        fvDimensionBuilder.setIpDst(data.ip_dst[0]);
        fvDimensionBuilder.setSrcPort(data.src_port[0]);
        fvDimensionBuilder.setDstPort(data.dst_port[0]);
        fvDimensionBuilder.setFrameCapLen(data.frame_cap_len[0]);
        msgType = (byte) FV_DIMENSION;
        doSend(fvDimensionBuilder.build().toByteArray(),bfos,msgType);
    }

    private void doSend(byte[] factData,BufferedOutputStream bfos,byte msgType){
        try {
            bfos.write(head);           //2 head
            bfos.write(msgType);        //1 msg type
            short len = (short) factData.length;
            bfos.write(ByteUtil.shortToByte(len));//2 byte data length
            bfos.write(factData);       //n fact data
            bfos.flush();
        } catch (IOException e) {
            System.err.println("send msg error [statisticBuilder], remote server socket may be closed");
        }
    }
}
