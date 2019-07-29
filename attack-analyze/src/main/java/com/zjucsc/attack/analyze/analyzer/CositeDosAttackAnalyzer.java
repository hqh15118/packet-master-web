package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.analyze.analyzer_util.CositeDosAttackAnalyzeList;

import java.util.Map;

public class CositeDosAttackAnalyzer extends TcpDosAttackAnalyzer<CositeDosAttackAnalyzeList> {
    /**
     * 通过构造函数注入分析结构
     *
     * @param t     key 源地址，value 分析的数据结构
     * @param clazz
     */
    public CositeDosAttackAnalyzer(Map<String, CositeDosAttackAnalyzeList> t, Class<CositeDosAttackAnalyzeList> clazz) {
        super(t, clazz);
    }
}
