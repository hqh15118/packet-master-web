package com.zjucsc.application.util;

import com.zjucsc.application.config.Common;

import javax.activation.CommandInfo;
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
        Common.CAPTURE_PROTOCOL.addAll(Arrays.asList(protocols));
    }

    public static synchronized Set<String> getCaptureProtocols(){
        return Common.CAPTURE_PROTOCOL;
    }

    public static void clearCaptureProtocols(){
        Common.CAPTURE_PROTOCOL.clear();
    }

    public static final byte[] LOCK = new byte[1];

    public static void removeTsharkProcess(Process process){
        synchronized (LOCK){
            Common.TSHARK_RUNNING_PROCESS.remove(process);
        }
    }

    public static void addTsharkProcess(Process process){
        synchronized (LOCK){
            Common.TSHARK_RUNNING_PROCESS.add(process);
        }
    }

    public static List<Process> findAllRunningTsharkProcess(){
        return Common.TSHARK_RUNNING_PROCESS;
    }

    public static void shotDownAllRunningTsharkProcess(){
        synchronized (LOCK){
            for (Process tsharkRunningProcess : Common.TSHARK_RUNNING_PROCESS) {
                tsharkRunningProcess.destroyForcibly();
            }
            System.out.println("exit " + Common.TSHARK_RUNNING_PROCESS.size() + " tshark process");
        }
    }
}
