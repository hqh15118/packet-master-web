package com.zjucsc.application.system.mapper.base;


import com.zjucsc.application.domain.bean.BaseResponse;

public interface BaseMapper<T extends BaseResponse> {
    T getById(Object id);
    BaseResponse deleteById(Object id);
    BaseResponse updateById(T t);
    BaseResponse insertById(T t);
}