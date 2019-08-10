package com.zjucsc.attack.util;

import com.zjucsc.attack.analyze.analyzer_util.AbstractDosList;
import com.zjucsc.attack.bean.DosConfig;
import com.zjucsc.tshark.FvDimensionList;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-31 - 22:28
 */
public abstract class BaseAttackAnalyzer<T extends AbstractDosList> implements IAttack {
    //分析需要用到的数据结构
    protected Map<String,T> t;
    protected Class<T> clazz;
    private DosConfig dosConfig;
    /**
     * 通过构造函数注入分析结构
     * @param t key 源地址，value 分析的数据结构
     * @param clazz
     */
    public BaseAttackAnalyzer(Map<String,T> t,Class<T> clazz){
        this.t = t;
        this.clazz = clazz;
    }

    //返回数据结构
    protected Map<String,T> getAnalyzer(){
        return t;
    }

    @Override
    public String analyze(FvDimensionLayer layer) throws IllegalAccessException, InstantiationException {
        if (validPacket(layer)){
            return doAnalyze(layer);
        }
        return null;
    }

    protected abstract boolean validPacket(FvDimensionLayer layer);

    /**
     * 先调用validPacket，只有validPacket返回true，才会调用下面的doAnalyze方法
     * @param layer 五元组
     * @return 攻击信息
     * @throws IllegalAccessException 异常1
     * @throws InstantiationException 异常2
     */
    @SuppressWarnings("unchecked")
    protected String doAnalyze(FvDimensionLayer layer) throws IllegalAccessException, InstantiationException {
        String srcIp = layer.ip_src[0];
        if (!srcIp.equals("--")){
            T analyzeList = getAnalyzer().get(srcIp);
            if (analyzeList == null){
                analyzeList = clazz.newInstance();
                analyzeList.setBaseAttackAnalyzer(this);
                getAnalyzer().put(srcIp,analyzeList);
            }
            return analyzeList.append(layer);
        }
        return null;
    }

    public DosConfig getDosConfig() {
        return dosConfig;
    }

    public BaseAttackAnalyzer setDosConfig(DosConfig dosConfig) {
        this.dosConfig = dosConfig;
        return this;
    }

    @Override
    public String toString() {
        return "BaseAttackAnalyzer{" +
                ", dosConfig=" + dosConfig +
                '}';
    }
}
