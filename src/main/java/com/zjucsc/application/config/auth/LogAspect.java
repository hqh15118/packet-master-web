package com.zjucsc.application.config.auth;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.base.util.HttpContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;

/**
 * AOP 记录用户操作日志
 *
 * @author hongqianhui
 */
@Slf4j
@Aspect
//@Component
public class LogAspect {

    @Autowired private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private ConstantConfig constantConfig;

    @Pointcut("@annotation(com.zjucsc.application.config.auth.Log)")
    public void pointcut() {
        // do nothing
    }

    private static ThreadLocal<StringBuilder> builderThreadLocal
            = ThreadLocal.withInitial(() -> new StringBuilder(20));

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        StringBuilder sb = builderThreadLocal.get();
        sb.delete(0,sb.length());

        Object result = null;
        long beginTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 执行方法
        result = point.proceed();
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (constantConfig.isOpenAOPLog()) {
            // 保存日志
            //TODO Kafka应该单独建一个service，分别发送重要的日志【重传】的和一般的日志
            //还需要加上查询日志等接口
            kafkaTemplate.send("log_info",JSON.toJSONString(LogBean.builder()
                    .clazzName(point.getTarget().getClass().getName())
                    .costTime(time)
                    .methodArgs(point.getArgs())
                    .methodName(signature.getMethod().getName())
                    .request(request)
                    .result(result)
                    .build()));
        }
        return result;
    }
}
