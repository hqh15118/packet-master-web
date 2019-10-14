package com.zjucsc.tshark.pre_processor2;

import com.zjucsc.common.util.ExceptionSafeRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class TsharkPreProcessor implements PreProfessor2{

    private TsharkListener tsharkListener;
    private NewDataCallback<String> newDataCallback;
    private String tsharkPath = "tshark";
    private String macAddress;
    private String interfaceName;
    private Process tsharkProcess;
    private Logger logger = LoggerFactory.getLogger(TsharkPreProcessor.class);
    private String bindCommand;
    private volatile boolean running = true;

    @Override
    public void registerPacketCallback(TsharkListener tsharkListener, NewDataCallback newDataCallback) {
        if (tsharkListener!=null){
            this.tsharkListener = tsharkListener;
        }
        if (newDataCallback !=null){
            this.newDataCallback = newDataCallback;
        }
    }

    @Override
    public synchronized void startTshark()  {
        if (bindCommand == null) {
            bindCommand = tsharkPath + " -l -n " +
                    "-T ek " +
                    "-f " + "\"not ether src " + macAddress + " \"" +
                    " -i " + interfaceName + " -Y s7comm";
        }
        try {
            tsharkProcess = Runtime.getRuntime().exec(bindCommand);
        } catch (IOException e) {
            logger.error("can not start tshark process ... " , e);
            tsharkListener.error(e.toString(),e);
        }
        InputStream tsharkErrorStream = tsharkProcess.getErrorStream();
        Thread tsharkErrorMsgThread = new Thread(new ExceptionSafeRunnable<Object>() {
            @Override
            public void run(Object o) {
                handleTsharkErrorMsg(tsharkErrorStream);
            }
        });
        tsharkErrorMsgThread.setName("-tshark-error-msg-" + tsharkErrorMsgThread.getThreadGroup() + "-" + tsharkErrorMsgThread.getId());
        tsharkErrorMsgThread.start();
    }

    private void startMainCapture(){
        Thread tsharkMainThread = new Thread(new ExceptionSafeRunnable<Object>() {
            @Override
            public void run(Object o) {
                handleTsharkMainMsg(tsharkProcess.getInputStream());
            }
        });
        tsharkMainThread.setName("-tshark-main-capture-" + tsharkMainThread.getThreadGroup() + "-" + tsharkMainThread.getId());
        tsharkMainThread.start();
    }

    private void handleTsharkMainMsg(InputStream inputStream) {
        try(BufferedReader bfReader = new BufferedReader(new InputStreamReader(inputStream))){
            for (;running;){
                String data = bfReader.readLine();
                if (data!=null && data.length() > 90){
                    newDataCallback.callback(data);
                }
            }
        }catch (IOException e) {
            logger.error("" , e);
            tsharkListener.error("error while reading tshark output...",e);
        }
    }

    private void handleTsharkErrorMsg(InputStream tsharkErrorStream) {
        try (BufferedReader bfReader = new BufferedReader(new InputStreamReader(tsharkErrorStream))) {
            String data;
            while ((data = bfReader.readLine()) != null) {
                if (!data.startsWith("Capturing")) {
                    tsharkListener.error(data,null);
                }else{
                    tsharkListener.success(bindCommand,tsharkProcess);
                    startMainCapture();
                }
            }
        } catch (IOException e) {
            logger.error("", e);
        }
    }

    @Override
    public void stopTshark() {
        tsharkProcess.destroy();
    }

    @Override
    public void restartTshark() {
        tsharkProcess.destroy();
        startTshark();
    }

    @Override
    public void setTsharkPath(String tsharkPath) {
        this.tsharkPath = tsharkPath;
    }

    @Override
    public void setCaptureInterface(String macAddress, String interfaceName) {
        this.macAddress = macAddress;
        this.interfaceName = interfaceName;
    }
}
