package com.zjucsc.base.util.cache;

public class CachedElementNotExistException extends RuntimeException {
    private String msg;
    public CachedElementNotExistException(String msg){
        super(msg);
    }
}
