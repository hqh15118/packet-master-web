package com.zjucsc.common.common_util;

public class CommonUtil {
    private static final ThreadLocal<StringBuilder> GLOBAL_THREAD_LOCAL_STRING_BUILDER =
            new ThreadLocal<StringBuilder>(){
                @Override
                protected StringBuilder initialValue() {
                    return  new StringBuilder(100);
                }
            };

    public static StringBuilder getGlobalStringBuilder(){
        StringBuilder sb = GLOBAL_THREAD_LOCAL_STRING_BUILDER.get();
        return sb.delete(0,sb.length());
    }
}
