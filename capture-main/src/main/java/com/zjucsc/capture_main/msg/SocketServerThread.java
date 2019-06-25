package com.zjucsc.capture_main.msg;

import com.zjucsc.msg_socket.BaseSocketConsumerThread;
import com.zjucsc.msg_socket.SocketStateCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;


public class SocketServerThread extends Thread {

    private SocketStateCallback socketStateCallback;

    public SocketServerThread(SocketStateCallback socketStateCallback){
        this.socketStateCallback = socketStateCallback;
    }

    @Override
    public void run() {
        String[] addressAndPort = null;
        try {
            addressAndPort = portAndbindAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerSocket serverSocket = null;
        try {
            assert addressAndPort!=null;
            assert addressAndPort[1]!=null;
            serverSocket = new ServerSocket();
            serverSocket.bind(InetSocketAddress.createUnresolved(addressAndPort[0],Integer.parseInt(addressAndPort[1])));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert serverSocket!=null;
        for (;;){
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("can not [accept] new socket connection in [SocketServerThread] , ServerSocket may be closed .. ");
            }
            if (socket==null){
                try {
                    sleep(1000);
                    continue;
                } catch (InterruptedException ignored) {
                }
            }
            SocketCenter.addNewSocket(socket);
            CustomSocketConsumerThread consumerThread = new CustomSocketConsumerThread(socket);
            consumerThread.setName("-socket-client-thread");
            consumerThread.setUncaughtExceptionHandler((t, e) -> {
                System.err.println(String.format("socket client thread : [%s] caught an exception %s",t.getName(),e.getStackTrace()[0].toString()));
            });
            consumerThread.start();
        }
    }

    private String[] portAndbindAddress() throws IOException {
        File file = new File("config/socket.properties");
        System.out.println(file.getAbsolutePath());
        if (!file.exists()){
            System.err.println("config/socket.properties文件不存在");
            throw new RuntimeException("config/socket.properties文件不存在");
        }
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        return new String[]{properties.getProperty("socket.server.bind.address"),
        properties.getProperty("socket.server.bind.port")};
    }
}
