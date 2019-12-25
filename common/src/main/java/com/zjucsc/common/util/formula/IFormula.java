package com.zjucsc.common.util.formula;

import javassist.CannotCompileException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * #project MLenningPro
 *
 * @author hongqianhui
 * @create_time 2019-12-02 - 12:23
 */
public interface IFormula {


    Object invokeMethod(Map<String, Float> artMap) throws InvocationTargetException, IllegalAccessException;

    void addFormula(String... formula) throws CannotCompileException;

    boolean removeFormula(String... formula) throws CannotCompileException;
}
