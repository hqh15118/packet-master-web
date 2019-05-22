package com.zjucsc;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IArtDecode extends Serializable {
    Map<String,Float> decode(Map<String,Float> globalMap,byte[] payload,Object...obj);
    String protocol();
    List<AttackType> attackDecode(List<AttackType> globalAttackList , byte[] payload , Object...obj);
}
