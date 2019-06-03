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
        return GLOBAL_THREAD_LOCAL_STRINGBUILDER.get();
    }
}
