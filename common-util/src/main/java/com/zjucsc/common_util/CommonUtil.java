package com.zjucsc.common_util;

import java.text.SimpleDateFormat;

public class CommonUtil {
    private static final ThreadLocal<StringBuilder> GLOBAL_THREAD_LOCAL_STRINGBUILDER =
            new ThreadLocal<StringBuilder>(){
                @Override
                protected StringBuilder initialValue() {
                    return  new StringBuilder(100);
                }
            };

    public static StringBuilder getGlobalStringBuilder(){
        StringBuilder sb = GLOBAL_THREAD_LOCAL_STRINGBUILDER.get();
        return sb.delete(0,sb.length());
    }
}
