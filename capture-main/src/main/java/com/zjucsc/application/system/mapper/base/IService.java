package com.zjucsc.application.system.mapper.base;

import com.zjucsc.application.domain.bean.BaseResponse;

public interface IService <U extends BaseResponse>{
    U getById(Object id);
    BaseResponse deleteById(Object id);
    BaseResponse updateById(U t);
    BaseResponse insertById(U t);
}
