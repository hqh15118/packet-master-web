package com.zjucsc.common.application.service;

/**
 *
 * @param <F> source object
 * @param <T> target object
 */
public interface Converter<F,T> {
    T convert(F source);
}
