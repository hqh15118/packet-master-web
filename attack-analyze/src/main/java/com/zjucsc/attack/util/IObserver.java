package com.zjucsc.attack.util;

import java.util.Collection;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:01
 */
public interface IObserver<T> {
    /**
     * 注册观察者
     * @param observable
     * @param <T>
     */
    void register(IObservable<T> observable);

    /**
     * 注册一组观察者
     * @param observables
     * @param <T>
     */
     void register(Collection<IObservable<T>> observables);

    /**
     * 解除观察者注册
     * @param observable
     */
    void unRegister(IObservable observable);

    /**
     * 通知所有的观察者
     */
    void updateAll(T t);

}
