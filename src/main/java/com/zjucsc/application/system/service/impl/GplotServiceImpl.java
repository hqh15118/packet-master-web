package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.system.entity.Gplot;
import com.zjucsc.application.system.mapper.GplotMapper;
import com.zjucsc.application.system.service.iservice.IGplotService;
import org.springframework.stereotype.Service;

/**
 * @author hongqianhui
 */
@Service("gplotservice")
public class GplotServiceImpl extends ServiceImpl<GplotMapper, Gplot> implements IGplotService {

}
