package com.zjucsc.msg_socket;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BaseSocketProducerThread<T> extends Thread{

    private final LinkedBlockingQueue<T> SEND_QUEUE
            = new LinkedBlockingQueue<>();

    private volatile boolean running = true;

    @Override
    public void run() {
        Socket socket = new Socket();
        String[] properties = null;
        try {
            properties = clientPortAndbindAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert properties!=null;

        try {
            socket.bind(InetSocketAddress.createUnresolved(properties[2],Integer.parseInt(properties[3])));
            socket.connect(InetSocketAddress.createUnresolved(properties[0],Integer.parseInt(properties[1])));
        } catch (IOException e) {
            System.err.println("client bind or connect remote address error!");
            e.printStackTrace();
        }

        BufferedOutputStream bfos = null;

        try {
            bfos = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert bfos!=null;

        for (;running;){
            try {
                T data = SEND_QUEUE.poll(1, TimeUnit.SECONDS);
                if (data!=null){
                    handleMsgFromQueue(bfos,data);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void handleMsgFromQueue(BufferedOutputStream bfos , T data) {
    }


    private String[] clientPortAndbindAddress() throws IOException {
        File file = new File("config/socket.properties");
        if (!file.exists()){
            System.err.println("config/socket.properties文件不存在");
            throw new RuntimeException("config/socket.properties文件不存在");
        }
        Properties properties = new Properties();
        try(InputStream is = new FileInputStream(file)){
            properties.load(is);
        }
        return new String[]{
                properties.getProperty("socket.client.remote.address"),
                properties.getProperty("socket.client.remote.port"),
                properties.getProperty("socket.client.bind.port"),
                properties.getProperty("socket.client.bind.address")
        };
    }

    public void appendData(T t){
        SEND_QUEUE.offer(t);
    }

    public void closeProducerThread(){
        running = false;
    }
}
