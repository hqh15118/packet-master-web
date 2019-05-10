package com.zjucsc.packetmasterweb.spring;

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

import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperationController {

    @Autowired private TsharkMainService tsharkMainService;
    @Autowired private PacketController packetController;
    @Autowired private OptFilterController optFilterController;
    @Autowired private FvDimensionFilterController fvDimensionFilterController;
    ApplicationTest addFvDimensionFilterRules = new ApplicationTest();

    @Test
    public void getTargetExistIdFilterTest() throws InterruptedException, ExecutionException, DeviceNotValidException, ProtocolIdNotValidException {
        //optFilterController.getOptFilter(10,1,0,2);
    }

    @Test
    public void deleteFilter() throws ProtocolIdNotValidException {
        optFilterController.deleteOptFilter(10 , 10 , 2);//delete funcode
        optFilterController.deleteOptFilter(10 , -1 , 2);//delete protocol
        optFilterController.deleteOptFilter(10 ,  -1 ,-1);//delete device
    }
}
