package com.zjucsc.application.system.service.hessian_mapper;

import com.zjucsc.application.domain.bean.Gplot;
import com.zjucsc.application.system.mapper.base.BaseMapper;

import java.util.List;

public interface GplotMapper extends BaseMapper<Gplot> {

    List<Gplot> selectAll();
}
