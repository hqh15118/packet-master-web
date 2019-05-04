package com.zjucsc.application.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("attack_info")
public class AttackInfo {
    @TableId("time")
    private String time;
    @TableField("type")
    private String type;
    //json of AttackMsg bean
    @TableField("attack_msg")
    private String message;

    @Data
    public static class AttackMsg{
        private String src_ip;
        private String dst_ip;
        private String src_mac;
        private String dst_mac;
        private String port;
        private String protocol;
    }
}
