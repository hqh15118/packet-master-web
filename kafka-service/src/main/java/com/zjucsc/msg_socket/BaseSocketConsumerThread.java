package com.zjucsc.msg_socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

/********************************************
 * 01111111 10001001    起始2字节，标记
 * msg_type             1个字节，数据类型
 *                      1.五元组；2.攻击信息；3.工艺参数；4.统计信息 【socket event】
 * msg_len              2个字节
 * data                 n个字节
 ********************************************/
public class BaseSocketConsumerThread extends Thread{

    private Socket socket;
    private BufferedInputStream bfis;

    public BaseSocketConsumerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        if (bfis == null){
            //init input and output stream
            try {
                bfis = new BufferedInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        handleBfis(bfis);
    }

    public void handleBfis(BufferedInputStream bfis) {

    }



}
