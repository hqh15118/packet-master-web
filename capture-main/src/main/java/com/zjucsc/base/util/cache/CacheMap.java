package com.zjucsc.base.util.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMap<K,V> {
    private Map<K,V> cacheMap;
    public CacheMap(){
        cacheMap = new ConcurrentHashMap<>();
    }
    public CacheMap(int cap){
        cacheMap = new ConcurrentHashMap<>(cap);
    }

    public CacheMap(Map<K,V> cache){
        cacheMap = cache;
    }

    /**
     * @param k
     * @param v 返回null表示insert，不为null表示update
     * @return
     */
    public V insertOrUpdate(K k,V v){
        return cacheMap.put(k,v);
    }

    /**
     * 返回被删除的那个元素
     * @param k
     * @return
     */
    public V delete(K k){
        return cacheMap.remove(k);
    }

    public V deleteExist(K k){
        V v = cacheMap.remove(k);
        if (v == null){
            throw new CachedElementNotExistException("[ " + k + " ] delete failed ==> " + " is not exist in cache");
        }
        return v;
    }

    public V select(K k){
        return cacheMap.get(k);
    }

    public V selectExist(K k){
        V v = cacheMap.get(k);
        if (v == null){
            throw new CachedElementNotExistException("[ " + k + " ] select null ==> " + " is not exist in cache");
        }
        return v;
    }
}
