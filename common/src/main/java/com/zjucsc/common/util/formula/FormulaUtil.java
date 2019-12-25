package com.zjucsc.common.util.formula;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * #project MLenningPro
 *
 * @author hongqianhui
 * @create_time 2019-11-24 - 15:46
 */
public class FormulaUtil extends AbstractFormulaUtil{
    private Method targetFormulaMethod = null;
    private Object instance;
    public FormulaUtil(){

    }

    @Override
    protected void rebuildFormulaMethod(CtClass reloadClassCt, String code) {
        try {
            reloadClassCt.writeFile("hot_swap");
            Class<?> reloadClass = loadTargetClass(lastName);
            targetFormulaMethod = reloadClass.getDeclaredMethod("artCheck",Map.class);
            instance = reloadClass.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IOException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invokeMethod(Map<String, Float> artMap) throws InvocationTargetException, IllegalAccessException {
        return targetFormulaMethod.invoke(instance,artMap);
    }


}
