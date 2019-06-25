package com.zjucsc.capture_main.bean;

import java.util.Map;


public class MsgWrapper<K,V> {
    private Map<K,V> map;
    private int type;

    public MsgWrapper(Map<K,V> map, int type) {
        this.map = map;
        this.type = type;
    }

    public Map<K,V> getMap() {
        return map;
    }

    public void setMap(Map<K,V> map) {
        this.map = map;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
