package com.zjucsc.application.system.mapper.base;


public interface BaseMapper<T> {
    T getById(Object id);
    void deleteById(Object id);
    void updateById(T t);
    void insertById(T t);
}