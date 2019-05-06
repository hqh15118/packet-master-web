package com.zjucsc.application.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zjucsc.application.tshark.domain.packet.FiveDimensionPacket;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;

/**
 * 组态配置实体类
 */
@Data
@TableName("operation_filter")
/**********************
 * 用户名 过滤器种类 - {"协议":[content1,content2]}
 * ********************
 */
//数据库中保存的数据格式
public class OperationFilterEntity {
    @TableId("device_id")
    private int deviceId;
   // @NotBlank(message = "过滤器种类不能为空")
    //组态配置内容
    //@TableField("protocol")
    //@NotBlank(message = "协议不能为空")
    //private String protocol;
    @NotBlank(message = "用户名不为空")
    @TableField("user_name")
    private String userName;
    //HashMap<protocol,list<filter>>
    @TableField("content")
    @NotBlank(message = "组态内容不能为空")
    private String content;

    //前端应该传递的数据格式
    @Data
    public static class OperationFilterForFront{
        private String userName;
        private int deviceId;
        private HashMap<Integer,List<OperationFilter>> protocolToFilterList;
        //private String protocol;
        //private List<OptFilter> operationFilters;
    }

    /******************************************************
     * OperationFilterForFront
     * {
     *     userName : xxx   用户名
     *     {
     *      protocol ： xxx  协议           该过滤器对应的协议如S7、modbus等
     *      [
     *          {
     *              filterType : 0 / 1        过滤类别：0表示白名单，1表示黑名单
     *              fun_code : 操作码          操作码，协议对应的功能码
     *          },
     *          ...
     *      ]
     *      ,
     *      ...
     *     }
     * }
     * ***************************************************/
    //规则实体
    @Data
    public static class OperationFilter{
        //白名单还是黑名单
        @Max(1)
        @Min(0)
        private int filterType;
        private int fun_code;
    }
}
