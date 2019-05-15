package com.zjucsc.application.util;

import com.zjucsc.application.config.Common;

import java.util.Set;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 20:59
 */
public class CommonTsharkUtil {
    public static synchronized void addCaptureProtocol(String protocol){
        Common.CAPTURE_PROTOCOL.add(protocol);
    }

    public static synchronized Set<String> getCaptureProtocols(){
        return Common.CAPTURE_PROTOCOL;
    }

    public static void clearCaptureProtocols(){
        Common.CAPTURE_PROTOCOL.clear();
    }
}
