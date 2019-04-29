package com.zjucsc.application.config;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.ArrayList;
import java.util.List;


public class Common {
    public static final int SOCKET_IO_PORT = 8081;

    public static final String CAPTURE_COMMAND = "C:\\Users\\Administrator\\Desktop\\tshark_min\\tshark.exe -l -n -e frame.protocols -e eth.dst -e eth.src -e ip.src -e ip.addr -e tcp.srcport -e tcp.dstport -e s7comm.param.func -e modbus.func_code -T ek -c 5 -r C:\\Users\\Administrator\\Desktop\\pcap_files\\question_1531953285_02.pcap";

    public static List<String> hasStartedHost = new ArrayList<>();

    private static List<SocketIOClient> connectedClient = new ArrayList<>();

    public synchronized static void addConnectedClient(SocketIOClient socketIOClient){
        connectedClient.add(socketIOClient);
    }

    public synchronized static void removeConnectedClient(SocketIOClient socketIOClient){
        connectedClient.remove(socketIOClient);
    }

    public static void updateAllClient(String event , Object...obj){
        for (SocketIOClient socketIOClient : connectedClient) {
            socketIOClient.sendEvent(event,obj);
        }
    }
}
