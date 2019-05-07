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

    private int i = 0;

    private String inetAddress ="default_service";

    private volatile boolean keep_running = true;

    public void start(String command ){
        start(command,null);
    }

    public void start(String command , ProcessCallback callback){
        start(command,inetAddress,callback);
    }

    public void start(String command ,String inetAddress,ProcessCallback callback){
        this.inetAddress = inetAddress;
        log.info("run command : {}" , command);
        Process process = PacketMain.runTargetCommand(command);
        PipeLine pipeLine = initPipeLine();
        System.out.println("pipe line config \n" + pipeLine);
        assert pipeLine!=null;
        Thread thread = new Thread(() -> {
            if (callback!=null){
                callback.start();
            }
            try (InputStream is = process.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                for (; ;) {
                    String str = "";
                    if ((str = reader.readLine()) != null && keep_running) {
                        if (str.length() > 85) {
                            pipeLine.pushDataAtHead(str);
                            i++;
                            Thread.sleep(1000);
                        }
                    } else {
                        break;
                    }
                }
                if (keep_running) {
                    System.out.println("quit by finish reading stream");
                }else {
                    System.out.println("quit by stop service");
                    keep_running = true;
                }
                if (callback!=null) {
                    callback.end(i , inetAddress);
                }
            } catch (RuntimeException | IOException e) {
                throw new OpenCaptureServiceException("can not get pipe output of tshark");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                process.destroy();
                System.out.println("process exit value :  " +  process.exitValue());
            }
        });
        thread.setName("-packet-service-");
        thread.start();
    }


    public void stop(){
        keep_running = false;
    }

    public interface ProcessCallback{
        void error(Exception e);
        void start();
        void end(Object...objs);
    }
}
