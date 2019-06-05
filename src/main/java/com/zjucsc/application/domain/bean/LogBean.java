package com.zjucsc.application.domain.bean;

import lombok.Builder;
import lombok.Data;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-22 - 23:31
 */

@Data
@Builder
public class LogBean  {
    private String clazzName;
    private String methodName;
    private long costTime;
    private String methodArgs;
    private String result;
    private int logType;
    private String exception;
    private String startTime;

    public static final int NORMAL_LOG = 1 , IMPORTANT_LOG = 2;
}
