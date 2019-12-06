package com.zjucsc.common.util;

public class ThreadLocalUtil {

    private static ThreadLocal<StringBuilder> stringBuilderThreadLocal
            = ThreadLocal.withInitial(() -> new StringBuilder(100));

    public static StringBuilder getStringBuilder(){
        StringBuilder sb = stringBuilderThreadLocal.get();
        return sb.delete(0 , sb.length());
    }
}
