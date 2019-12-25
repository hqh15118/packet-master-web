package com.zjucsc.common.util.formula;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * #project MLenningPro
 *
 * @author hongqianhui
 * @create_time 2019-11-27 - 22:23
 */
public class FormulaUtilTest {


    public static void main(String[] args) throws CannotCompileException, InvocationTargetException, IllegalAccessException, NotFoundException, InterruptedException {
//        IFormula formulaUtil = new FormulaUtil();
        IFormula formulaUtil = new FormulaUtilPlus();
        formulaUtil.addFormula("([水位1]>10 && [电压1]==105) && [水位2]==10", "([水位1]>10 && [电压1]==105) || [水位2]==10",
                "[水位1]==10" );
        Map<String,Float> artMap = new HashMap<String,Float>(){
            {
                put("水位1",00.0F);
                put("电压1",105F);
                put("水位2",11.0F);
                put("水位100",0F);
            }
        };
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            formulaUtil.invokeMethod(artMap);
        }
        long time2 = System.currentTimeMillis();
        System.gc();
        formulaUtil.addFormula("([水位1]==0 && [电压1]==105) || [水位2]==11");
        long time3 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            formulaUtil.invokeMethod(artMap);
        }
        System.out.println(formulaUtil.invokeMethod(artMap));
        System.out.println("remove formula : [" + formulaUtil.removeFormula("([水位1]==0 && [电压1]==105) || [水位2]==11") + "]");
        System.out.println(formulaUtil.invokeMethod(artMap));
        long time4 = System.currentTimeMillis();
        System.out.println(time2 - time1);
        System.out.println(time4 - time3);
        for (int i = 0; i < 1; i++) {
            formulaUtil.addFormula("([水位1]==0 && [电压1]==105) || [水位2]==10");
        }
        formulaUtil.addFormula("([水位100]==0 && [电压1]==105) && [水位2]==11");
        System.out.println(formulaUtil.invokeMethod(artMap));
        Thread.sleep(10000000);

    }

}
