package com.zjucsc.application.config;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 19:57
 */

/**
 * 过滤器种类，根据什么规则进行过滤
 * 1、FIVE_DIMENSION 五元组过滤
 * 2、OPERATION_XXX  功能码协议
 * 3、ARTIFACT_PARAM 工艺参数
 */
public enum FilterType {
    FIVE_DIMENSION("five_dimension"),
    OPERATION_MODBUS("operation_modbus"),
    OPERATION_S7Comm_JOB("operation_s7Comm_job"),
    OPERATION_S7Comm_Ack_data("operation_ack_data"),
    ARTIFACT_PARAM("artifact_param");

    private String comment;

    FilterType(String comment) {
        this.comment = comment;
    }
    public String getComment(){
        return comment;
    }
}
