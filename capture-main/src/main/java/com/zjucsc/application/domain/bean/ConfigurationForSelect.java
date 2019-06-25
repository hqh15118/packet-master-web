package com.zjucsc.application.domain.bean;

import lombok.Data;


@Data
public class ConfigurationForSelect{
    public String codeDes;     //查询条件
    public int page;           //页
    public int limit;          //数量
    public int protocolId;   //协议
}
