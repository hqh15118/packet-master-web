package com.zjucsc.application.config.auth;


import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.domain.bean.BaseResponse;
import com.zjucsc.application.domain.exceptions.DataAccessException;
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
