package com.zjucsc.application.tshark.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 16:17
 */
public class OperationPacketFilter<K,V> {
    private Map<K,V> whiteMap = new ConcurrentHashMap<>();
    private Map<K,V> blackMap = new ConcurrentHashMap<>();
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

    public Map<K, V> getWhiteMap() {
        return whiteMap;
    }

    public void setWhiteMap(Map<K, V> whiteMap) {
        this.whiteMap = whiteMap;
    }

    public Map<K, V> getBlackMap() {
        return blackMap;
    }

    public void setBlackMap(Map<K, V> blackMap) {
        this.blackMap = blackMap;
    }

    @Override
    public String toString() {
        return "OperationPacketFilter{" +
                "whiteMap=" + whiteMap +
                //", blackMap=" + blackMap +
                ", filterName='" + filterName + '\'' +
                '}';
    }
}
