package com.zjucsc.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class CommonUtil {

    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

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
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMddHHmmssSS"));
    public static SimpleDateFormat getDateFormat2(){
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL2.get();
    }
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT_THREAD_LOCAL3
            = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS"));
    public static SimpleDateFormat getDateFormat3(){
        return SIMPLE_DATE_FORMAT_THREAD_LOCAL3.get();
    }

}
