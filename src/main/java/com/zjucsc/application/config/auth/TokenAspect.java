package com.zjucsc.application.config.auth;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.exceptions.AuthNotValidException;
import com.zjucsc.application.domain.exceptions.TokenNotValidException;
import com.zjucsc.application.system.service.hessian_iservice.UserOptService;
import com.zjucsc.base.util.HttpContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class TokenAspect {

    @Autowired private UserOptService userOptService;

    @Pointcut("@annotation(com.zjucsc.application.config.auth.Token)")
    public void pointcut() {
        // do nothing
    }

    //&&@annotation(token)" , argNames = "token,point"
    @Before(value = "pointcut()&&@annotation(token)")
    public void before(JoinPoint point , Token token) throws TokenNotValidException, AuthNotValidException {
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        log.info("===============请求内容===============");
        log.info("request url:" + request.getRequestURL().toString());
        log.info("request method:" + request.getMethod());
        log.info("request class method:" + point.getSignature());
        log.info("request args :" + Arrays.toString(point.getArgs()));
        String request_token = request.getParameter("token");
        String request_role = userOptService.getTokenRole(request_token);
        if (request_role == null){
            throw new TokenNotValidException("数据库中找不到token : " + token);
        }
        int request_role_id = Common.AUTH_MAP.inverse().get(request_role);
        if (request_role_id < token.value()){
            throw new AuthNotValidException("权限不足");
        }
    }
}
