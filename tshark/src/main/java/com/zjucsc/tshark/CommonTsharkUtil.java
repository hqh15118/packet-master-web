package com.zjucsc.tshark;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 20:59
 */
public class CommonTsharkUtil {

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

    public static void removeTsharkProcess(Process process){
        synchronized (LOCK){
            TsharkCommon.TSHARK_RUNNING_PROCESS.remove(process);
        }
    }

    public static void addTsharkProcess(Process process){
        synchronized (LOCK){
            TsharkCommon.TSHARK_RUNNING_PROCESS.add(process);
        }
    }

    public static List<Process> findAllRunningTsharkProcess(){
        return TsharkCommon.TSHARK_RUNNING_PROCESS;
    }

    public static void shotDownAllRunningTsharkProcess(){
        synchronized (LOCK){
            for (Process tsharkRunningProcess : TsharkCommon.TSHARK_RUNNING_PROCESS) {
                tsharkRunningProcess.destroyForcibly();
            }
            TsharkCommon.TSHARK_RUNNING_PROCESS.clear();
            System.out.println("exit " + TsharkCommon.TSHARK_RUNNING_PROCESS.size() + " tshark process");
        }
    }

}
