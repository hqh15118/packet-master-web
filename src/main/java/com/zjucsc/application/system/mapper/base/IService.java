package com.zjucsc.application.system.mapper.base;

public interface IService <U>{
    U getById(Object id);
    void deleteById(Object id);
    void updateById(U t);
    void insertById(U t);
}
