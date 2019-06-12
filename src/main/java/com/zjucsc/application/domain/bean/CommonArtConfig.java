package com.zjucsc.application.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class CommonArtConfig {

    /**
     * protocol : 2
     * show_graph : 0
     * tag : 参数
     * data : {"id":"","type":"int","length":null,"addr_head":null,"reg_coil":null,"range":[2,22],"details":"备注"}
     */
    @JSONField(name = "protocol")
    private int protocolId;
    @JSONField(name = "show_graph")
    private int showGraph;
    private String tag;
    private Object data;
    @JSONField(name = "id")
    private int artConfigId;
}
