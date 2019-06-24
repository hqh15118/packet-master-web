package com.zjucsc.art_decode.artconfig;

import com.zjucsc.art_decode.base.BaseConfig;

public class OpcuaConfig extends BaseConfig {
    private String type; //变量类型
    private String name; //变量名称 TODO:在OPCUA的类中，可能需要单独的变量名称位置
    private int clientHandle;
    private int monitoredItemId;
    private int requestHandle; //TODO:不确定是否需要把该变量放在这里


    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setClientHandle(int clientHandle) {
        this.clientHandle = clientHandle;
    }

    public void setMonitoredItemId(int monitoredItemId) {
        this.monitoredItemId = monitoredItemId;
    }

    public void setRequestHandle(int requestHandle) {
        this.requestHandle = requestHandle;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getClientHandle(){
        return clientHandle;
    }

    public int getMonitoredItemId(){
        return monitoredItemId;
    }

    public int getRequestHandle(){
        return requestHandle;
    }
}
