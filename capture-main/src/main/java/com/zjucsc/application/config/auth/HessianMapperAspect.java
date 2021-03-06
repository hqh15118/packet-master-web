package com.zjucsc.application.config.auth;


import com.zjucsc.application.config.properties.ConstantConfig;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.base.util.HttpUtil;
import com.zjucsc.common.exceptions.DataAccessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect

public class HessianMapperAspect {

    @Autowired private ConstantConfig constantConfig;

    /**
     * hessian_mapper目录下的任意类的任意方法
     * public * ： public方法的任意返回值
     */
    @Pointcut("execution(public * com.zjucsc.application.system.service.hessian_mapper.*.*(..))")
    public void mapper(){

    }

    @Around("mapper()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        if (constantConfig.isOpenMapperAop() && result instanceof BaseResponse){
            BaseResponse response = ((BaseResponse) result);
            if (response.code != 200) {
                throw new DataAccessException(response.code, response.msg);
            }
        }
        return result;
    }
}
