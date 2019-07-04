package com.zjucsc.common.common_util;

import java.text.SimpleDateFormat;

public class CommonUtil {
    private static final ThreadLocal<StringBuilder> GLOBAL_THREAD_LOCAL_STRING_BUILDER =
            new ThreadLocal<StringBuilder>(){
                @Override
                protected StringBuilder initialValue() {
                    return new StringBuilder(100);
                }
            };

    public static StringBuilder getGlobalStringBuilder(){
        StringBuilder sb = GLOBAL_THREAD_LOCAL_STRING_BUILDER.get();
        return sb.delete(0,sb.length());
    }

    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL
            = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };
    public static SimpleDateFormat getDateFormat(){
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL.get();
    }

    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL2
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    public static SimpleDateFormat getDateFormat2(){
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL2.get();
    }
}
