package com.zjucsc.application.system.controller;

import com.caucho.hessian.client.HessianProxyFactory;
import com.zjucsc.application.domain.bean.ArtHistoryData;
import com.zjucsc.application.system.service.iservice.IArtHistoryData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.*;

//
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ArtHistoryDataControllerTest {

    @Autowired
    private IArtHistoryData iArtHistoryData;

    @Test
    public void getArtHistoryData() throws InterruptedException {
        iArtHistoryData.getArtData("2019-05-29 08:47:00", "2019-05-29 19:57:00", "test","year");
        //iArtHistoryData.saveArtData("test_art_name",1.0F,new byte[]{1,2,3});
        Thread.sleep(100000000);
    }


    @Test
    public void getArtHistoryData2() throws InterruptedException, MalformedURLException {
        String art_history_data = "http://10.15.191.100:36955/hessian/ArtHistoryData.hessian";
        IArtHistoryData iArtHistoryData = (IArtHistoryData) new HessianProxyFactory().create(IArtHistoryData.class, art_history_data);
        System.out.println(iArtHistoryData);
        iArtHistoryData.saveArtData("test_art_name",1.0F,new byte[]{1,2,3});
        long time1 = System.currentTimeMillis();
        List<ArtHistoryData> list = iArtHistoryData.getArtData("2019-05-29 08:47:00","2019-05-29 19:57:00","test","test");
        System.out.println(list);
        System.out.println(System.currentTimeMillis() - time1);
    }
}