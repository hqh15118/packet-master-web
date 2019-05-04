package com.zjucsc.application.util;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-02 - 21:35
 */
public abstract class AbstractAnalyzer<T> implements Analyzed {
    private T analyzer;
    public void setAnalyzer(T t){
        this.analyzer = t;
    }

    public AbstractAnalyzer(T t){
        this.analyzer = t;
    }

    public T getAnalyzer(){
        return analyzer;
    }
}
