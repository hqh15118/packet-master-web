package com.zjucsc.attack.util;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:06
 */
public abstract class BaseObserver<T> implements IObserver<T> {
    protected CopyOnWriteArraySet<IObservable<T>> copyOnWriteArraySet
            = new CopyOnWriteArraySet<>();

}
