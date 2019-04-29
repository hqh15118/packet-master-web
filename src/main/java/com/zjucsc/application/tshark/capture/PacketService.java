package com.zjucsc.application.tshark.capture;


import com.zjucsc.application.tshark.capture.PacketMain;

import java.io.IOException;

public class PacketService {
    public static void openPacketService(String command){
        Process process = PacketMain.runTargetCommand(command) ;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PacketMain.doProcess(process);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
