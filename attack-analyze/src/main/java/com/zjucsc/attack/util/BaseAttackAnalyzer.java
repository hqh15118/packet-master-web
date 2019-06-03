package com.zjucsc.attack.util;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-31 - 22:28
 */
public abstract class BaseAttackAnalyzer<T> implements IAttack {
    //分析需要用到的数据结构
    protected T t;

    //通过构造函数注入分析结构
    public BaseAttackAnalyzer(T t){
        this.t = t;
    }

    //返回数据结构
    protected T getAnalyzer(){
        return t;
    }
}
