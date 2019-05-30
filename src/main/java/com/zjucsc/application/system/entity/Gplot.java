package com.zjucsc.application.system.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName("gplot")
public class Gplot implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private int id;
    @TableField("name")
    private String name;
    @TableField("info")
    private String info;
    @TableField("update_time")
    private String updateTime;


    @Override
    public String toString() {
        return "Gplot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public static class GplotForFront{
        public String name;
        public String info;
    }
}
