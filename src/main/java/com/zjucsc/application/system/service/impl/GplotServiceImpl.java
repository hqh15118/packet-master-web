package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.system.entity.Gplot;
import com.zjucsc.application.system.mapper.GplotMapper;
import com.zjucsc.application.system.service.iservice.IGplotService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hongqianhui
 */
@Service("gplotservice")
public class GplotServiceImpl extends ServiceImpl<GplotMapper, Gplot> implements IGplotService {

    @Transactional
    @Override
    public void addNewGplot(Gplot gplot) {
        Map<String,Object> removeMap = new HashMap<>();
        removeMap.put("name" , gplot.getName());
        removeByMap(removeMap);
        save(gplot);
    }

    @Async(value = "global_single_thread_executor")
    @Override
    public void changeGplot(int gplotId) {
        //TODO
    }
}
