package com.zjucsc.capture_main.msg;

import com.google.protobuf.InvalidProtocolBufferException;
import com.zjucsc.capture_main.protobuf.ArtArgData;
import com.zjucsc.capture_main.protobuf.AttackBeanOuterClass;
import com.zjucsc.capture_main.protobuf.FvDimension;
import com.zjucsc.capture_main.protobuf.StatisticInfoOuterClass;
import com.zjucsc.common_util.ByteUtil;
import com.zjucsc.msg_socket.BaseSocketConsumerThread;
import com.zjucsc.socket_io.SocketServiceCenter;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

import static com.zjucsc.capture_main.msg.CustomSocketConsumerThread.MSG_TYPE.*;
import static com.zjucsc.socket_io.SocketIoEvent.*;

public class CustomSocketConsumerThread extends BaseSocketConsumerThread {

    private volatile boolean running = true;

    public CustomSocketConsumerThread(Socket socket) {
        super(socket);
    }

    @Override
    public void handleBfis(BufferedInputStream bfis) {
        byte[] head1 = new byte[1];
        byte[] len = new byte[2];
        byte[] data = new byte[4096];
        try {
            for (; running; ) {
                bfis.read(head1);
                if (head1[0] != 0x7F) {
                    continue;
                }
                bfis.read(head1);
                if (head1[0] != 0x09) {
                    continue;
                }
                bfis.read(head1);
                int msgType = Byte.toUnsignedInt(head1[0]);
                bfis.read(len);
                short length = ByteUtil.bytesToShort(len,0);
                if (length > 1024){
                    System.err.println("4096 is too short and : " + length);
                }
                int readLength = bfis.read(data,0,length);
                switch (msgType){
                    case FV_DIMENSION:
                        doWithFvDimension(data,readLength);
                        break;
                    case ATTACK:
                        doWithAttack(data,readLength);
                        break;
                    case ART_ARG:
                        doWithArtArg(data,readLength);
                        break;
                    case STATISTICS_INFO:
                        doWithStatisticsInfo(data,readLength);
                        break;
                    default:
                        System.err.println("can not do with msg type : " + msgType);
                        break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private com.google.protobuf.Parser<StatisticInfoOuterClass.StatisticInfo> statisticParser
            = StatisticInfoOuterClass.StatisticInfo.parser();

    private com.google.protobuf.Parser<ArtArgData.ArtArg> artArgParser = ArtArgData.ArtArg.parser();

    private com.google.protobuf.Parser<AttackBeanOuterClass.AttackBean> attackBeanParser =
            AttackBeanOuterClass.AttackBean.parser();

    private com.google.protobuf.Parser<FvDimension.FvDimensionLayer> fvDimensionParser = FvDimension.FvDimensionLayer.parser();

    private void doWithStatisticsInfo(byte[] data, int readLength) throws InvalidProtocolBufferException {
        SocketServiceCenter.updateAllClient(STATISTICS_PACKET,statisticParser.parseFrom(data,0,readLength));
    }

    private void doWithArtArg(byte[] data, int readLength) throws InvalidProtocolBufferException {
        SocketServiceCenter.updateAllClient(ART_INFO,artArgParser.parseFrom(data,0,readLength));
    }

    private void doWithAttack(byte[] data, int readLength) throws InvalidProtocolBufferException {
        SocketServiceCenter.updateAllClient(ATTACK_INFO,attackBeanParser.parseFrom(data,0,readLength));
    }

    private void doWithFvDimension(byte[] data, int readLength) throws InvalidProtocolBufferException {
        SocketServiceCenter.updateAllClient(ALL_PACKET,fvDimensionParser.parseFrom(data,0,readLength));
    }

    public void stopSocket(){
        running = false;
    }

    static final class MSG_TYPE{
        static final int FV_DIMENSION = 1;
        static final int ATTACK = 2;
        static final int ART_ARG = 3;
        static final int STATISTICS_INFO = 4;
    }
}
