package com.zjucsc.attack.util;

import com.zjucsc.attack.bean.AttackBean;

import java.util.Collection;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-30 - 20:10
 */
public class Observer extends BaseObserver<AttackBean> {
    @Override
    public void register(IObservable<AttackBean> observable) {
        copyOnWriteArraySet.add(observable);
    }

    @Override
    public void register(Collection<IObservable<AttackBean>> iObservables) {
        copyOnWriteArraySet.addAll(iObservables);
    }

    @Override
    public void unRegister(IObservable observable) {
        copyOnWriteArraySet.remove(observable);
    }

    @Override
    public void updateAll(AttackBean attackBean) {
        for (IObservable<AttackBean> attackBeanIObservable : copyOnWriteArraySet) {
            attackBeanIObservable.update(attackBean);
        }
    }
}
