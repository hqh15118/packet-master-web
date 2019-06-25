package com.zjucsc.socket_io;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

public class MainServer {

    private static boolean hasStartedService = false;
    private static SocketIOServer server = null;
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
                Thread thread = new Thread(() -> {
                    Configuration config = new Configuration();
                    config.setHostname(ip);
                    config.setPort(port);
                    server = new SocketIOServer(config);
                    server.addConnectListener(connectListener);
                    server.addDisconnectListener(disconnectListener);
                    server.start();
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (InterruptedException ignored) {

                    }
                    server.stop();
                    server = null;
                });
                thread.setName("-web-socket-server-");
                thread.start();
            }
        }
        return true;
    }

    public static boolean close(){
        if (!hasStartedService){
            return false;
        }else{
            hasStartedService = false;
            if (server!=null){
                server.stop();
                server = null;
                return true;
            }else{
                return false;
            }
        }
    }

    public static boolean getWebSocketServiceState(){
        return hasStartedService;
    }
}
