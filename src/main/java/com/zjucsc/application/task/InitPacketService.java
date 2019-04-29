package com.zjucsc.application.task;

import com.zjucsc.application.system.service.impl.PacketServiceImpl;
import com.zjucsc.base.util.SpringContextUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitPacketService implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        PacketServiceImpl impl = SpringContextUtil.getBean("packet_service", PacketServiceImpl.class);
        impl.getAllNetworkInterface();
    }
}
