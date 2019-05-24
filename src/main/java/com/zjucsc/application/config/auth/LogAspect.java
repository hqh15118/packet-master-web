package com.zjucsc.application.config.auth;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.config.SocketIoEvent;
import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.application.socketio.SocketServiceCenter;
import com.zjucsc.application.system.service.iservice.IKafkaService;
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
@Component
public class LogAspect {

    @Autowired private IKafkaService iKafkaService;

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
        Exception exception = null;
        StringBuilder sb = builderThreadLocal.get();
        sb.delete(0,sb.length());
        Object result = null;
        long beginTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) point.getSignature();
        // 执行方法
        try {
            result = point.proceed();
        }catch (Exception e){
            exception = e;
        }
        // 获取 request
        HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (constantConfig.isSendAopLog()) {
            // 保存日志
            //TODO Kafka应该单独建一个service，分别发送重要的日志【重传】的和一般的日志
            //还需要加上查询日志等接口
//            kafkaTemplate.send("log_info",JSON.toJSONString(LogBean.builder()
////                    .clazzName(point.getTarget().getClass().getName())
////                    .costTime(time)
////                    .methodArgs(point.getArgs())
////                    .methodName(signature.getMethod().getName())
////                    .result(result)
////                    .build()));
        }
        if (constantConfig.isOpenAOPLog()){
            sb.append("****************\n请求类-方法：")
                    .append(point.getTarget().getClass().getName()).append("-").append(signature.getMethod().getName())
                    .append("\n")
                    .append("请求参数:")
                    .append(JSON.toJSONString(point.getArgs()))
                    .append("\n")
                    .append("请求结果:")
                    .append(JSON.toJSONString(result))
                    .append("\n");
            if (exception != null){
                sb.append("异常：>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
                    .append(exception.getClass().getName()).append("--")
                        .append(exception.getMessage())
                        .append("\n");
            }
            sb.append("****************");
            log.info("info : \n {}",sb.toString());
        }
        return result;
    }
}
