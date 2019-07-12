package com.zjucsc.application.config.auth;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.config.ConstantConfig;
import com.zjucsc.application.config.KafkaTopic;
import com.zjucsc.application.domain.bean.LogBean;
import com.zjucsc.kafka.KafkaThread;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * AOP 记录用户操作日志
 *
 * @author hongqianhui
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    private final KafkaThread<LogBean> LOG_BEAN_SEND_KAFKA_THREAD =
            KafkaThread.createNewKafkaThread("normal_log", KafkaTopic.SEND_NORMAL_LOG);

    {
        LOG_BEAN_SEND_KAFKA_THREAD.startService();
    }

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
        //HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        if (constantConfig.isSendAopLog()) {
            LogBean.LogBeanBuilder builder = LogBean.builder()
                    .methodArgs(JSON.toJSONString(point.getArgs()))
                    .result(JSON.toJSONString(result))
                    .clazzName(point.getTarget().getClass().getName())
                    .methodName(signature.getMethod().getName())
                    .logType(LogBean.NORMAL_LOG)
                    .costTime(time)
                    .startTime(new Date(beginTime).toString());
            if (exception!=null){
                builder.exception(exception.getMessage());
            }
            LOG_BEAN_SEND_KAFKA_THREAD.sendMsg(builder.build());
        }
        if (constantConfig.isOpenAOPLog()){
            if (constantConfig.isShowErrorOnly()){
                if (exception==null){
                    return result;
                }
            }
                sb.append("****************\n请求类-方法：")
                        .append(point.getTarget().getClass().getName()).append("-").append(signature.getMethod().getName())
                        .append("\n")
                        .append("请求参数:")
                        .append(JSON.toJSONString(point.getArgs()))
                        .append("\n")
                        .append("请求结果:")
                        .append(JSON.toJSONString(result))
                        .append("\n")
                        .append("耗时：")
                        .append(time)
                        .append("\n");
                if (exception != null){
                    sb.append("异常：>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n")
                        .append(exception.getClass().getName()).append("--")
                            .append(exception.getStackTrace()[0].toString())
                            .append("\n");
                }
                sb.append("****************");
                log.info("\n{}",sb.toString());
                if (exception!=null){
                    log.error("detail \n{}: " , exception , "↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
                }
        }
        return result;
    }
}
