package com.zjucsc.tshark;

import com.zjucsc.tshark.bean.CollectorState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class TsharkCommon {

    public final static ConcurrentHashMap<Integer, CollectorState> COLLECTOR_STATE_MAP = new ConcurrentHashMap<>();

    public static String OS_NAME = System.getProperty("os.name");

    public static String filter = "tcp";

    public static String sessionReset = "10000";

    public static Thread.UncaughtExceptionHandler uncaughtExceptionHandler
            = (t, e) -> System.out.println("thread " + t.getName() + " caught an exception " + e);

    public static final Set<String> CAPTURE_PROTOCOL = new HashSet<>();

    public static final List<Process> TSHARK_RUNNING_PROCESS = new ArrayList<>();

    public static int getErrorProcessThreadNumber(){
        return CACHED_ERROR_STREAM_POOL.getActiveCount();
    }

    private static final TsharkErrorThreadPool CACHED_ERROR_STREAM_POOL =
            new TsharkErrorThreadPool(6, 12, 10, TimeUnit.SECONDS,
                    new SynchronousQueue<>(), (r, executor) -> System.err.println("task over flow in tshark error thread pool!"));

    private static ErrorCallback errorCallback;

    private static final byte[] LOCK1 = new byte[1];

    public static void handleTsharkErrorStream(String comment,final BufferedReader bfr){
        assert errorCallback!=null;
        CACHED_ERROR_STREAM_POOL.setName(comment).execute(() -> {
            try{
                String errorMsg;
                StringBuilder sb = new StringBuilder(100);
                for (;;){
                    errorMsg = bfr.readLine();
                    if (errorMsg==null){
                        break;
                    }else{
                        synchronized (LOCK1){
                            errorCallback.errorCallback(sb.append(comment).append(":").append(errorMsg).toString());
                            sb.delete(0,sb.length());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bfr!=null){
                    try {
                        bfr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void setErrorCallback(ErrorCallback errorCallback){
        TsharkCommon.errorCallback = errorCallback;
    }

    public interface ErrorCallback{
        void errorCallback(String errorMsg);
    }
}
