package com.zjucsc.attack.base;

import com.zjucsc.attack.bean.AttackBean;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.concurrent.ConcurrentSkipListSet;

public abstract class BaseOptAnalyzer<T extends BaseOptConfig> implements IOptAttackEntry {
    private final ConcurrentSkipListSet<T> configs =
            new ConcurrentSkipListSet<>();

    /**
     * 添加工艺参数配置
     * @param a 工艺参数配置
     */
    public void addOptAnalyzeConfig(T a){
        configs.add(a);
    }

    /**
     * update
     * @param a config
     */
    public void updateOptAnalyzeConfig(T a){
        configs.remove(a);
        configs.add(a);
    }

    /**
     * 删除工艺参数配置
     * @param t 工艺参数
     */
    public void deleteOptAnalyzeConfig(T t){
        configs.remove(t);
    }

    @Override
    public AttackBean analyze(FvDimensionLayer layer, Map<String,Float> techmap , Object... objs) {
        AttackBean attackBean = null;
        for (T config : configs) {
            attackBean = doAnalyze(layer,techmap,config,objs);
            if (attackBean!=null){
                return attackBean;
            }
        }
        return null;
    }

    public abstract AttackBean doAnalyze(FvDimensionLayer layer,Map<String,Float>techmap, T t, Object... objs);

}
