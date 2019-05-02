package com.zjucsc.application.socketio;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainServer {

    private static boolean hasStartedService = false;

    public static boolean openWebSocketService(String ip , int port, ConnectListener connectListener,
                                            DisconnectListener disconnectListener){
        if (hasStartedService){
            return false;
        }
        synchronized (Boolean.class){
            if (hasStartedService){
                return false;
            }else{
                hasStartedService = true;
                //FIXME 只是简陋地这样写了一下
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Configuration config = new Configuration();
                        config.setHostname(ip);
                        config.setPort(port);
                        final SocketIOServer server = new SocketIOServer(config);
                        server.addConnectListener(connectListener);
                        server.addDisconnectListener(disconnectListener);
                        server.start();
                        try {
                            Thread.sleep(Integer.MAX_VALUE);
                        } catch (InterruptedException ignored) {
                        }
                        server.stop();
                    }
                }).start();
            }
        }
        return true;
    }
}
