package com.zjucsc.application.tshark.handler;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.domain.bean.CollectorState;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.tshark.decode.AbstractAsyncHandler;
import com.zjucsc.application.tshark.domain.beans.PacketInfo;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacketWrapper;
import com.zjucsc.application.tshark.domain.packet.layers.ModbusPacket;
import com.zjucsc.application.tshark.domain.packet.layers.OtherPacket;
import com.zjucsc.application.tshark.domain.packet.layers.S7Packet;
import com.zjucsc.application.tshark.domain.packet.layers.TcpPacket;
import com.zjucsc.application.util.PacketDecodeUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static com.zjucsc.application.config.PACKET_PROTOCOL.*;

/**
 * 功能说明：1、将tshark得到的JSON数组转为报文实体传递给BadPacketAnalyzeHandler分析报文；
 * 2、单独的collectorStateChangePool中，用于分析报文payload中传递的数据采集器的运行状态，
 * 如果检测到运行状态发生改变，就向前端发送消息；
 * @see BadPacketAnalyzeHandler
 */
public class PacketDecodeHandler extends AbstractAsyncHandler<FiveDimensionPacketWrapper> {

    private ThreadLocal<StringBuilder[]> stringBuilderThreadLocal = new ThreadLocal<StringBuilder[]>(){
        @Override
        protected StringBuilder[] initialValue() {
            return new StringBuilder[]{new StringBuilder(100),new StringBuilder(100)};
        }
    };


    private ExecutorService collectorStateChangePool = Executors.newSingleThreadExecutor(new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("-collector-state-change-sender-pool-");
            return thread;
        }
    });

    public PacketDecodeHandler(ExecutorService executor) {
        super(executor);
    }

    /* FiveDimensionPacketWrapper fields :
    *   timeStamp,      五元组
    *   protocol,       五元组
    *   src_ip,         五元组
    *   dis_ip,         五元组
    *   code            五元组
    *   tcpPayload      TCP负载
    * */
    @Override
    public FiveDimensionPacketWrapper handle(Object t) {
        PacketInfo.PacketWrapper wrapper = (PacketInfo.PacketWrapper) t;
        /*
         * 这部分是给Pcap准备的，如果检测到收到的fiveDimensionPacketWrapper不为空，
         * 则表示式从pcapHandler发过来的，那么就直接返回进入下一阶段不需要解析了
         */
        if (((PacketInfo.PacketWrapper) t).fiveDimensionPacketWrapper!=null){
            return ((PacketInfo.PacketWrapper) t).fiveDimensionPacketWrapper;
        }
        //System.out.println("decode packet handler : " + wrapper);
        final byte[] payload = PacketDecodeUtil.hexStringToByteArray2(wrapper.tcpPayload);
        collectorStateChangePool.execute(new Runnable() {
            @Override
            public void run() {
                CollectorState state = PacketDecodeUtil.decodeCollectorState(payload,24);
                if (state != null){
                    SocketServiceCenter.updateAllClient(SocketIoEvent.COLLECTOR_STATE , state);
                }
            }
        });
        //这里的wrapper.packetProtocol都是PACLET_PROTOCOL中定义的标准格式
        switch (wrapper.packetProtocol) {
            case TCP:
                return decodeTcpPacket(wrapper.packetProtocol,wrapper.json , payload);
            case MODBUS:
                return decodeModbusPacket(wrapper.packetProtocol,wrapper.json , payload);
            case S7_Ack_data:
            case S7_JOB:
            case S7:
                return decodeS7commPacket(wrapper.packetProtocol,wrapper.json , payload);
            default: {
                return decodeOtherPacket(wrapper.packetProtocol,wrapper.json , payload);
            }
        }
    }

    private FiveDimensionPacketWrapper decodeModbusPacket(String protocol , String packetJSON , byte[] payload){
        ModbusPacket modbusPacket = JSON.parseObject(packetJSON, ModbusPacket.class);
        ModbusPacket.LayersBean layers = modbusPacket.layers;
        StringBuilder sb = clearBuilder0();
        StringBuilder sb1 = clearBuilder1();
        return new FiveDimensionPacketWrapper.Builder()
                .timeStamp(PacketDecodeUtil.decodeTimeStamp(payload , 20))
                .protocol(protocol)
                .srcEthAndIp(sb.append(layers.eth_src[0]).append("-").append(layers.ip_src[0]).toString())
                .dstEthAndIp(sb1.append(layers.eth_dst[0]).append("-").append(layers.ip_addr[0]).toString())
                .src_Ip(layers.ip_src[0])
                .dst_Ip(layers.ip_addr[0])
                .fun_code(layers.modbus_func_code[0])
                .tcpPayload(payload)
                .dis_port(layers.tcp_dstport[0])
                .src_port(layers.tcp_srcport[0])
                .packetLength(layers.frame_cap_len[0])
                .build();
    }

    private FiveDimensionPacketWrapper decodeS7commPacket(String protocol , String packetJSON , byte[] payload){
        S7Packet s7Packet = JSON.parseObject(packetJSON, S7Packet.class);
        S7Packet.LayersBean layers = s7Packet.layers;
        StringBuilder sb = clearBuilder0();
        StringBuilder sb1 = clearBuilder1();
        return new FiveDimensionPacketWrapper.Builder()
                .timeStamp(PacketDecodeUtil.decodeTimeStamp(payload, 20))
                .protocol(protocol)
                .srcEthAndIp(sb.append(layers.eth_src[0]).append("-").append(layers.ip_src[0]).toString())
                .dstEthAndIp(sb1.append(layers.eth_dst[0]).append("-").append(layers.ip_addr[0]).toString())
                .src_Ip(layers.ip_src[0])
                .dst_Ip(layers.ip_addr[0])
                .fun_code(layers.s7comm_param_func[0])
                .tcpPayload(payload)
                .dis_port(layers.tcp_dstport[0])
                .src_port(layers.tcp_srcport[0])
                .packetLength(layers.frame_cap_len[0])
                .build();
    }

    private final byte[] blankPayload = new byte[]{};
    private final String blank = "";

    private FiveDimensionPacketWrapper decodeOtherPacket(String protocol , String packetJSON , byte[] payload){
        OtherPacket otherPacket = JSON.parseObject(packetJSON, OtherPacket.class);
        OtherPacket.LayersBean layers = otherPacket.layersBean;
        StringBuilder sb = clearBuilder0();
        StringBuilder sb1 = clearBuilder1();
        return new FiveDimensionPacketWrapper.Builder()
                .timeStamp(PacketDecodeUtil.decodeTimeStamp(payload, 20))
                .protocol(protocol)
                .srcEthAndIp(sb.append(layers.eth_src[0]).append("-").append(layers.ip_src[0]).toString())
                .dstEthAndIp(sb1.append(layers.eth_dst[0]).append("-").append(layers.ip_addr[0]).toString())
                .src_Ip(layers.ip_src[0])
                .dst_Ip(layers.ip_addr[0])
                .fun_code(blank)
                .tcpPayload(payload)
                .dis_port(layers.tcp_dstport[0])
                .src_port(layers.tcp_srcport[0])
                .packetLength(layers.frame_cap_len[0])
                .build();
    }

    private FiveDimensionPacketWrapper decodeTcpPacket(String protocol , String packetJSON , byte[] payload){
        TcpPacket tcpPacket = JSON.parseObject(packetJSON, TcpPacket.class);
        TcpPacket.LayersBean layers = tcpPacket.layers;
        StringBuilder sb = clearBuilder0();
        StringBuilder sb1 = clearBuilder1();
        return new FiveDimensionPacketWrapper.Builder()
                .timeStamp(PacketDecodeUtil.decodeTimeStamp(payload, 20))
                .protocol(protocol)
                .srcEthAndIp(sb.append(layers.eth_src[0]).append("-").append(layers.ip_src[0]).toString())
                .dstEthAndIp(sb1.append(layers.eth_dst[0]).append("-").append(layers.ip_addr[0]).toString())
                .src_Ip(layers.ip_src[0])
                .dst_Ip(layers.ip_addr[0])
                .fun_code(blank)
                .tcpPayload(blankPayload)
                .dis_port(layers.tcp_dstport[0])
                .src_port(layers.tcp_srcport[0])
                .packetLength(layers.frame_cap_len[0])
                .build();
    }

    private StringBuilder clearBuilder0(){
        StringBuilder sb = stringBuilderThreadLocal.get()[0];
        sb.delete(0,sb.length());
        return sb;
    }

    private StringBuilder clearBuilder1(){
        StringBuilder sb = stringBuilderThreadLocal.get()[1];
        sb.delete(0,sb.length());
        return sb;
    }

}
