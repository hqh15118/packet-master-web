package com.zjucsc.tshark;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Common {

    public static String OS_NAME = System.getProperty("os.name");

    public static Thread.UncaughtExceptionHandler uncaughtExceptionHandler
            = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("thread " + t.getName() + " caught an exception " + e);
        }
    };

    public static final Set<String> CAPTURE_PROTOCOL = new HashSet<>();

    public static final List<Process> TSHARK_RUNNING_PROCESS = new ArrayList<>();
}
