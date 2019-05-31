package com.zjucsc.application.system.mapper.base;

public class BaseServiceImpl<S extends BaseMapper<U>, U> implements IService<U>{
    protected S baseMapper;

    @Override
    public U getById(Object id) {
        return baseMapper.getById(id);
    }

    @Override
    public void deleteById(Object id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void updateById(U t) {
        baseMapper.updateById(t);
    }

    @Override
    public void insertById(U t) {
        baseMapper.insertById(t);
    }
}
