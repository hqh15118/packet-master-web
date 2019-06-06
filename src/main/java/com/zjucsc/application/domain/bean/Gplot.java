package com.zjucsc.application.domain.bean;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class Gplot extends BaseResponse implements Serializable {
    private int id;
    private String name;
    private String info;
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
