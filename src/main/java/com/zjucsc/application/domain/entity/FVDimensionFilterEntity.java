package com.zjucsc.application.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 17:46
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("five_dimension_filter")
public class FVDimensionFilterEntity {
    @TableId("device_id")
    private int deviceId;
    @TableField("content")
    private String content;
    @TableField("user_name")
    private String user_name;

    @Data
    public static class FiveDimensionFilterForFront{
        private String userName;
        private int deviceId;
        private List<FiveDimensionFilter> fiveDimensionFilters;
    }

    @Data
    public static class FiveDimensionFilter{
        //白名单还是黑名单
        private int filterType;
        private int protocolId;
        private String dst_port;
        private String src_port;
        private String src_ip;
        private String dst_ip;
    }
}
