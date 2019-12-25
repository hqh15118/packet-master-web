package com.zjucsc.common.util.formula;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.IOException;
import java.util.Map;

/**
 * #project MLenningPro
 *
 * @author hongqianhui
 * @create_time 2019-12-02 - 12:18
 */
public class FormulaUtilPlus extends AbstractFormulaUtil{

    private ArtCheckInvoker artCheckInvoker;

    /**
     * 面向接口实现
     * @param reloadClassCt 重载class的修改器
     * @param code method 代码
     */
    @Override
    protected void rebuildFormulaMethod(CtClass reloadClassCt,String code) {
        try {
            //往目标类中添加接口，接口方法名和之前添加的method的方法名相同
            CtClass artCheckCtInterface = pool.getCtClass("com.zjucsc.common.util.formula.ArtCheckInvoker");
            reloadClassCt.setInterfaces(new CtClass[]{artCheckCtInterface});
            //写入本地文件
            reloadClassCt.writeFile("hot_swap");
            //用自定义的类加载器加载class文件，不用AppClassLoader防止内存泄漏
            artCheckInvoker = (ArtCheckInvoker)loadTargetClass(lastName).newInstance();
        } catch (NotFoundException | IllegalAccessException | InstantiationException | CannotCompileException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invokeMethod(Map<String, Float> artMap) {
        return artCheckInvoker.artCheck(artMap);
    }

}
