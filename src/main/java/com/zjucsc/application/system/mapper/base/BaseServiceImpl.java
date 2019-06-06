package com.zjucsc.application.system.mapper.base;


import com.zjucsc.application.domain.bean.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseServiceImpl<U extends BaseResponse,S extends BaseMapper<U>> implements IService<U>{

    @Autowired
    protected S baseMapper;

    @Override
    public U getById(Object id) {
        return baseMapper.getById(id);
    }

    @Override
    public BaseResponse deleteById(Object id) {
        return baseMapper.deleteById(id);
    }

    @Override
    public BaseResponse updateById(U t) {
        return baseMapper.updateById(t);
    }

    @Override
    public BaseResponse insertById(U t) {
        return baseMapper.insertById(t);
    }
}
