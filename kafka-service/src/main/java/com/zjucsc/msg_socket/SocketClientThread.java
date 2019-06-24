package com.zjucsc.msg_socket;

import java.net.Socket;

public class SocketClientThread extends Thread{

    private Socket socket;

    public SocketClientThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
