package com.zjucsc.application.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.system.entity.Art;
import com.zjucsc.application.system.mapper.ArtMapper;
import com.zjucsc.application.system.service.iservice.IArtService;
import org.springframework.stereotype.Service;

/**
 * @author hongqianhui
 */
@Service
public class ArtServiceImpl extends ServiceImpl<ArtMapper, Art> implements IArtService {

}
