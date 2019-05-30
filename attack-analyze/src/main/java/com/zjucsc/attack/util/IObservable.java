package com.zjucsc.attack.util;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:01
 */
public interface IObservable<T> {
    void update(T t);
}
