package com.zjucsc.application.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("gplot")
public class Gplot {
    @TableId(value = "device_id",type = IdType.AUTO)
    private int device_id;
    @TableField("device_name")
    private String device_name;
    @TableField("info")
    private String info;


    public static class GplotForFront{
        public String device_name;
        public String info;
    }
}
