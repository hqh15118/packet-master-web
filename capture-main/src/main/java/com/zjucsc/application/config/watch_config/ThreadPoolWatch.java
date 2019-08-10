package com.zjucsc.application.config.watch_config;


import com.zjucsc.application.system.service.common_iservice.CapturePacketService;
import com.zjucsc.application.util.ThreadPoolUtil;
import com.zjucsc.common.bean.ThreadPoolInfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.Map;

//@Endpoint(id = "thread_pool_config")
//@Configuration
public class ThreadPoolWatch {
//    @Autowired private CapturePacketService capturePacketService;
//    @ReadOperation
//    public List<ThreadPoolInfoWrapper> getThreadPoolServiceInfo(){
//        return ThreadPoolUtil.getThreadPoolInfos();
//    }
//
//    @SuppressWarnings("unchecked")
//    @ReadOperation
//    public Map<String,Integer> getMainHandlerThreadPoolPayload(){
//        return capturePacketService.load();
//    }
}
