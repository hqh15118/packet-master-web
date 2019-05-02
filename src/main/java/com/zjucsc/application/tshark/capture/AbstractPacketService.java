package com.zjucsc.application.tshark.capture;

import com.zjucsc.application.domain.exceptions.OpenCaptureServiceException;
import com.zjucsc.application.tshark.decode.PipeLine;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-04-30 - 22:23
 */
@Slf4j
public abstract class AbstractPacketService {

    public abstract PipeLine initPipeLine();

    public void start(String command){
        log.info("run command : {}" , command);
        Process process = PacketMain.runTargetCommand(command);
        PipeLine pipeLine = initPipeLine();
        System.out.println("pipe line config \n" + pipeLine);
        assert pipeLine!=null;
        new Thread(() -> {
            try (InputStream is = process.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                for (; ; ) {
                    String str = "";
                    if ((str = reader.readLine()) != null) {
                        if (str.length() > 120)
                            pipeLine.pushDataAtHead(str);
                    } else {
                        break;
                    }
                }
                System.out.println("quit");
            } catch (RuntimeException | IOException e) {
                throw new OpenCaptureServiceException("can not get pipe output of tshark");
            } finally {
                process.destroy();
                System.out.println("process exit value : {} " +  process.exitValue());
            }
        }).start();
    }
}
