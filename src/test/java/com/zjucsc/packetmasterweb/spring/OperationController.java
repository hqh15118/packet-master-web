package com.zjucsc.packetmasterweb.spring;

import com.alibaba.fastjson.JSON;
import com.zjucsc.application.system.entity.FvDimensionFilter;
import com.zjucsc.application.system.entity.OptFilter;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.controller.FvDimensionFilterController;
import com.zjucsc.application.system.controller.OptFilterController;
import com.zjucsc.application.system.controller.PacketController;
import com.zjucsc.application.system.service.TsharkMainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static com.zjucsc.application.config.Common.CAPTURE_COMMAND_WIN;
import static com.zjucsc.application.config.PACKET_PROTOCOL.MODBUS_ID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationController {

    @Autowired private TsharkMainService tsharkMainService;
    @Autowired private PacketController packetController;
    @Autowired private OptFilterController optFilterController;
    @Autowired private FvDimensionFilterController fvDimensionFilterController;
    ApplicationTest addFvDimensionFilterRules = new ApplicationTest();

    @Test
    public void operation_packet_filter_test() throws InterruptedException, ProtocolIdNotValidException, DeviceNotValidException, ExecutionException {
        packetController.startRecvRealTimePacket();
        Thread.sleep(1000);

        OptFilter filter1 = new OptFilter();
        OptFilter filter2 = new OptFilter();
        filter1.setFun_code(1);
        filter1.setFilterType(1);
        filter2.setFun_code(4);
        filter2.setFilterType(0);
        filter1.setDeviceId(10);
        filter2.setDeviceId(10);

        OptFilter.OptFilterForFront operationFilterForFront
                = new OptFilter.OptFilterForFront();
        operationFilterForFront.setProtocolId(MODBUS_ID);

        operationFilterForFront.setOptFilterList(Arrays.asList(
                filter1 , filter2
        ));
        System.out.println(JSON.toJSON(optFilterController.addNewOptFilter(Collections.singletonList(operationFilterForFront))));

        tsharkMainService.start(CAPTURE_COMMAND_WIN);
        Thread.sleep(20000);
        System.out.println("clear operation config");
        filter1.setFilterType(0);
        operationFilterForFront.setOptFilterList(Arrays.asList(
                filter1 , filter2
        ));
        optFilterController.addNewOptFilter(Collections.singletonList(operationFilterForFront));
        Thread.sleep(20000);

        FvDimensionFilter filter12 = new FvDimensionFilter();
        filter12.setFilter_type(1);
        filter12.setSrc_ip("192.168.254.134");
        filter12.setDst_port("502");
        filter12.setSrc_port("1075");
        filter12.setProtocol_id(MODBUS_ID);
        filter12.setDst_ip("192.168.254.143");
        filter12.setDeviceId(10);
        filter12.setUser_name("hongqianhui");
        System.out.println(fvDimensionFilterController.addFvDimensionFilterRules(
                Collections.singletonList(filter12)
        ));
        System.out.println("add fv filter config");
        Thread.sleep(30000000);
    }

    @Test
    public void operation_packet_filter_test1() throws InterruptedException, ProtocolIdNotValidException, DeviceNotValidException, ExecutionException {
        tsharkMainService.start(CAPTURE_COMMAND_WIN);
        Thread.sleep(30000000);
    }

}
