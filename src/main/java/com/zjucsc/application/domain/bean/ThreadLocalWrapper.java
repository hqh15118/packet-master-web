package com.zjucsc.application.domain.bean;

import com.zjucsc.art_decode.AttackType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ThreadLocalWrapper {
    private Map<String,Float> floatMap;
    private List<AttackType> attackTypeList;

}
