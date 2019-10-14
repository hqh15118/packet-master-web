package com.zjucsc.tshark;

import com.zjucsc.tshark.bean.ProcessWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class TsharkCommon {
    public interface ErrorCallback{
        void errorCallback(String errorMsg);
    }

    public static String OS_NAME = System.getProperty("os.name");

    public static String filter = "tcp";

    public static String modbus_filter = null;

    public static String s7comm_filter = null;

    public static String sessionReset = "100000";

    public static final Set<String> CAPTURE_PROTOCOL = new HashSet<>();

    public static final List<ProcessWrapper> TSHARK_RUNNING_PROCESS = new ArrayList<>();

//    public static int getErrorProcessThreadNumber(){
//        return CACHED_ERROR_STREAM_POOL.getActiveCount();
//    }

//    private static final TsharkErrorThreadPool CACHED_ERROR_STREAM_POOL =
//            new TsharkErrorThreadPool(6, 12, 10, TimeUnit.SECONDS,
//                    new SynchronousQueue<>(), (r, executor) -> System.err.println("task over flow in tshark error thread pool!"));

    private static ErrorCallback errorCallback;

//    private static final byte[] LOCK1 = new byte[1];

//    public static void handleTsharkErrorStream(String comment,final BufferedReader bfr){
//        assert errorCallback!=null;
//        CACHED_ERROR_STREAM_POOL.setName(comment).execute(() -> {
//            try{
//                String errorMsg;
//                StringBuilder sb = new StringBuilder(100);
//                for (;;){
//                    errorMsg = bfr.readLine();
//                    if (errorMsg==null){
//                        break;
//                    }else{
//                        synchronized (LOCK1){
//                            errorCallback.errorCallback(sb.append(comment).append(":").append(errorMsg).toString());
//                            sb.delete(0,sb.length());
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (bfr!=null){
//                    try {
//                        bfr.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }

    public static void setErrorCallback(ErrorCallback errorCallback){
        TsharkCommon.errorCallback = errorCallback;
    }


    public static synchronized void addCaptureProtocol(String[] protocols){
        TsharkCommon.CAPTURE_PROTOCOL.addAll(Arrays.asList(protocols));
    }

    public static synchronized Set<String> getCaptureProtocols(){
        return TsharkCommon.CAPTURE_PROTOCOL;
    }

    public static void clearCaptureProtocols(){
        TsharkCommon.CAPTURE_PROTOCOL.clear();
    }

    public static final byte[] LOCK = new byte[1];

    public static void removeTsharkProcess(ProcessWrapper processWrapper){
        synchronized (LOCK){
            TsharkCommon.TSHARK_RUNNING_PROCESS.remove(processWrapper);
        }
    }

    public static void addTsharkProcess(ProcessWrapper processWrapper){
        synchronized (LOCK){
            TsharkCommon.TSHARK_RUNNING_PROCESS.add(processWrapper);
        }
    }

    public static List<ProcessWrapper> findAllRunningTsharkProcess(){
        return TsharkCommon.TSHARK_RUNNING_PROCESS;
    }

    public static void shotDownAllRunningTsharkProcess(){
        synchronized (LOCK){
            for (ProcessWrapper tsharkRunningProcess : TsharkCommon.TSHARK_RUNNING_PROCESS) {
                tsharkRunningProcess.getProcess().destroyForcibly();
            }
            TsharkCommon.TSHARK_RUNNING_PROCESS.clear();
        }
    }

}
