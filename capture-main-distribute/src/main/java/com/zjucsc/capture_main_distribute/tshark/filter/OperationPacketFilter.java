package com.zjucsc.capture_main_distribute.tshark.filter;

import java.util.HashMap;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 16:17
 */
public class OperationPacketFilter<K,V> {
    private HashMap<K,V> whiteMap = new HashMap<>();
    private HashMap<K,V> blackMap = new HashMap<>();
    private String filterName;

    public OperationPacketFilter(String filterName){
        this.filterName = filterName;
    }

    public void addWhiteRule(K key,V value){
        whiteMap.put(key,value);
    }

    public void addBlackRule(K key,V value){
        blackMap.put(key,value);
    }

    public HashMap<K, V> getWhiteMap() {
        return whiteMap;
    }

    public void setWhiteMap(HashMap<K, V> whiteMap) {
        this.whiteMap = whiteMap;
    }

    public HashMap<K, V> getBlackMap() {
        return blackMap;
    }

    public void setBlackMap(HashMap<K, V> blackMap) {
        this.blackMap = blackMap;
    }

    @Override
    public String toString() {
        return "OperationPacketFilter{" +
                "whiteMap=" + whiteMap +
                ", blackMap=" + blackMap +
                ", filterName='" + filterName + '\'' +
                '}';
    }
}
