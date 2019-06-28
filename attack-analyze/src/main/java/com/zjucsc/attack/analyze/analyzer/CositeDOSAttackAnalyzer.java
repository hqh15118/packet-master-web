package com.zjucsc.attack.analyze.analyzer;

import com.zjucsc.attack.analyze.analyzer_util.CositeDOSAttackAnalyzeList;
import com.zjucsc.attack.util.BaseAttackAnalyzer;
import com.zjucsc.tshark.packets.FvDimensionLayer;

import java.util.Map;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-31 - 22:30
 */
public class CositeDOSAttackAnalyzer extends BaseAttackAnalyzer<Map<String, CositeDOSAttackAnalyzeList>> {


    public CositeDOSAttackAnalyzer(Map<String, CositeDOSAttackAnalyzeList> stringCositeDOSAttackAnalyzeListMap) {
        super(stringCositeDOSAttackAnalyzeListMap);
    }

    @Override
    public String analyze(FvDimensionLayer layer) {
        String dstIp = layer.ip_dst[0];
        if (dstIp.length() > 0){
            CositeDOSAttackAnalyzeList analyzeList = getAnalyzer().get(dstIp);
            if (analyzeList == null){
                analyzeList = new CositeDOSAttackAnalyzeList();
                getAnalyzer().put(dstIp,analyzeList);
            }
            return analyzeList.append(layer);
        }
        return null;
    }
}
