package com.zjucsc.capture_main.msg;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketCenter {
    private static final List<Socket> ALL_CONNECTED_SOCKET = new ArrayList<>();
    private static final byte[] LOCK = new byte[1];
    public static void addNewSocket(Socket socket){
        synchronized (LOCK){
            ALL_CONNECTED_SOCKET.add(socket);
        }
    }
    public static void removeOldSocket(Socket socket){
        synchronized (LOCK){
            ALL_CONNECTED_SOCKET.remove(socket);
        }
    }
}
