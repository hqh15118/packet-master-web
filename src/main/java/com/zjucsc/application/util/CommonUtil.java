package com.zjucsc.application.util;

import java.text.SimpleDateFormat;

public class CommonUtil {
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL
             = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:MM:ss");
        }
    };
    public static SimpleDateFormat getDateFormat(){
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL.get();
    }
}
