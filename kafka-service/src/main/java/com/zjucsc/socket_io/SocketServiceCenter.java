package com.zjucsc.socket_io;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;

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

    public static <T> void updateFvDimensionLayerClient(String event,AckCallback<T> ackCallback,Object...obj ){
        for (SocketIOClient socketIOClient : connectedClient) {
            socketIOClient.sendEvent(event, ackCallback , obj);
        }
    }

    public static Set<SocketIOClient> getAllClients(){
        return connectedClient;
    }

    @SuppressWarnings("unchecked")
    public static void registerSocketIoDataListenter(String eventName, Class<?> eventType, DataListener dataListener){
        MainServer.getServer().addEventListener(eventName,eventType,dataListener);
    }
}
