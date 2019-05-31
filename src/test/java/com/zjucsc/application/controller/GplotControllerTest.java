package com.zjucsc.application.controller;

import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.entity.Gplot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class GplotControllerTest {

    @Autowired private GplotController gplotController;

    @Test
    public void addGplotInfo() {
        Gplot.GplotForFront gplotForFront = new Gplot.GplotForFront();
        gplotForFront.info = "gplot_test_info";
        gplotForFront.name = "test_gplot";
        System.out.println(gplotController.addGplotInfo(gplotForFront));
    }


    @Test
    public void loadAllGplotInfo() {
    }

    @Test
    public void updateGplotInfo() {
    }

    @Test
    public void deleteGplotInfo() {
    }

    @Test
    public void setGplotId() throws ProtocolIdNotValidException, InterruptedException {
        gplotController.setGplotId(33);
        Thread.sleep(1000000000);
    }
}