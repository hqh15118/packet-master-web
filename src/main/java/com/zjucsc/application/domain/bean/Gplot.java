package com.zjucsc.application.domain.bean;


import lombok.Data;

import java.io.Serializable;

@Data
public class Gplot implements Serializable {
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
}
