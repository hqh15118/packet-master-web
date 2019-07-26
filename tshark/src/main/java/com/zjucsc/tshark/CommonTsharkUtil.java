package com.zjucsc.tshark;

import com.zjucsc.tshark.bean.ProcessWrapper;

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
