package com.zjucsc.application.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("gplot")
public class Gplot {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    @TableField("name")
    private String name;
    @TableField("info")
    private String info;


    public static class GplotForFront{
        public String name;
        public String info;
    }
}
