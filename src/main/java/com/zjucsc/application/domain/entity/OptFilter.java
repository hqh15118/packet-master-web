package com.zjucsc.application.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@TableName("operation_filter")
public class OptFilter {
    @TableId("id")
    private int id;
    @TableField("user_name")
    private String user_name;
    @TableField("device_id")
    @NotBlank
    private int deviceId;
    @TableField("filter_type")
    private int filterType;
    @TableField("fun_code")
    private int fun_code;
    @TableField("protocol_id")
    private int protocol_id;

    @Data
    public static class OptFilterForFront{
        private int protocolId;
        private String userName;
        private List<OptFilter> optFilterList;
    }

}
