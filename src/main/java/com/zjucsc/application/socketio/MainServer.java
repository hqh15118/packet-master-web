package com.zjucsc.application.socketio;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.BaseResponse;

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
                //FIXME 只是简陋地这样写了一下
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                });
                thread.setName("-websocket-server-");
                thread.start();
            }
        }
        return true;
    }

    public static BaseResponse close(){
        if (!hasStartedService){
            return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.SYS_ERROR,"服务未打开");
        }else{
            hasStartedService = false;
            if (server!=null){
                server.stop();
                server = null;
                return BaseResponse.OK();
            }else{
                return BaseResponse.ERROR(Common.HTTP_STATUS_CODE.SYS_ERROR,"程序错误");
            }
        }
    }

    public static boolean getWebSocketServiceState(){
        return hasStartedService;
    }
}
