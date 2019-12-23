package com.zjucsc.application.util.formula;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * #project MLenningPro
 *
 * @author hongqianhui
 * @create_time 2019-12-02 - 12:27
 */
public abstract class AbstractFormulaUtil implements IFormula{

    private final ConcurrentHashMap<String,String> FORMULA_MAP = new ConcurrentHashMap<>();

    protected static ClassPool pool = ClassPool.getDefault();

    protected String lastName;

    private CtClass cc;

    protected Class<?> loadTargetClass(String className){
        URL url = null;
        try {
            url = new File("hot_swap").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
        try {
            return urlClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     *
     * 水位 > 1
     * &
     * =
     * <
     * !
     * ()
     * |
     *
     * e.g.
     * （[水位1]>10 & [电压1]=105) & [水位2]=10
     *
     *
     *
     *
     * @return
     */
    private String getClassName(){
        return "com.tonggong.ArtCheckClass_" + new Date().toString();
    }

    /**
     * 对已经加入方法的method的class作进一步修改
     * @param rebuildClass
     * @param code
     */
    protected abstract void rebuildFormulaMethod(CtClass rebuildClass,String code);

    protected CtClass reloadClass(String code){
        //每次要重新创建一个class，旧的cc无法被修改，可以通过
        String className = getClassName();
        //删除旧的class文件
        if(lastName!=null) {
            try {
                Files.delete(Paths.get("hot_swap", lastName.replace(".","/") + ".class"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        lastName = className;
        //创建新的class_ct
        cc = pool.makeClass(className);
        try {
            //创建新的method(目标方法)
            CtMethod checkArtArgMethod = CtMethod.make(code,cc);
            cc.addMethod(checkArtArgMethod);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        return cc;
    }


    /**
     *
     * 水位 > 1
     * &
     * =
     * <
     * !
     * ()
     * |
     *
     * e.g.
     * （[水位1]>10 & [电压1]=105) & [水位2]=10
     *
     *
     *
     *
     * @return
     */
    @Override
    public void addFormula(String... formula) {
        for (String s : formula) {
            FORMULA_MAP.put(s,appendIfStatement(processPureFormula(s)));
        }
        String resultCode = appendStartAndEnd(FORMULA_MAP.values());
        CtClass ctClass = reloadClass(resultCode);
        rebuildFormulaMethod(ctClass,resultCode);
        ctClass.detach();
    }

    private static String appendStartAndEnd(Collection<String> formulas){
        StringBuilder sb = new StringBuilder();
        sb.append("public boolean artCheck(java.util.Map artMap){\n");
        for (String formula : formulas) {
            sb.append(formula);
        }
        sb.append("return false;}");
        return sb.toString();
    }

    private String appendIfStatement(String processedFormula){
        return "if(" + processedFormula + ")\n"+"  return true;\n";
    }

    private String processPureFormula(String formula){
        //（[水位1]>10 & [电压1]=105) & [水位2]=10 --> （[水位1]>10 && [电压1]==105) && [水位2]==10
        formula = formula.replace("（","(")
                .replace("）","(")
                .replace("&","&&")
                .replace("|","||")
                .replace("=","==");
        //（[水位1]>10 & [电压1]=105) & [水位2]=10 --> （[水位1]>10 && [电压1]==105) && [水位2]==10 -->
        //(artMap.get(水位1)>10&&artMap.get(电压1)=105)&&artMap.get(水位2)==10
        return formula.replace("[","((Float)artMap.get(\"")
                .replace("]","\")).floatValue()");
    }

    @Override
    public boolean removeFormula(String formula){
        return FORMULA_MAP.remove(formula) == null;
    }
}
