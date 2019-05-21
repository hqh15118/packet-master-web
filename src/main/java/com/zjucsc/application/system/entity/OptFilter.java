package com.zjucsc.application.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@TableName("opt_filter")
public class OptFilter {
    @TableId(value = "id" , type = IdType.AUTO)
    private int id;
    @TableField("user_name")
    private String user_name;
    @TableField("device_number")
    @NotBlank
    private String deviceNumber;
    @NotBlank
    @TableField("filter_type")
    private int filterType;
    @TableField("fun_code")
    private int fun_code;
    @TableField("protocol_id")
    private int protocol_id;
    @TableField("gplot_id")
    private int gplotId;

    @Data
    public static class OptFilterForFront{
        private int protocolId;
        private String userName;
        private int deviceId;
        private List<OptFilter> optFilterList;
    }

}
