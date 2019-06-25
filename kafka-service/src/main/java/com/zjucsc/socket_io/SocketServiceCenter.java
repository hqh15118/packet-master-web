package com.zjucsc.socket_io;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 01:26
 */
public class SocketServiceCenter {
    private static CopyOnWriteArraySet<SocketIOClient> connectedClient = new CopyOnWriteArraySet<>();

    public static void addConnectedClient(SocketIOClient socketIOClient){
        connectedClient.add(socketIOClient);
    }

    public static void removeConnectedClient(SocketIOClient socketIOClient){
        connectedClient.remove(socketIOClient);
    }

    public static void updateAllClient(String event , Object...obj){
        for (SocketIOClient socketIOClient : connectedClient) {
            socketIOClient.sendEvent(event,obj);
        }
    }

    public static Set<SocketIOClient> getAllClients(){
        return connectedClient;
    }
}
