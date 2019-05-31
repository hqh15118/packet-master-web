package com.zjucsc.application.controller;

import com.caucho.hessian.client.HessianProxyFactory;
import com.zjucsc.application.domain.bean.Gplot;
import com.zjucsc.application.system.service.hessian_iservice.IArtHistoryData;
import com.zjucsc.application.system.service.hessian_mapper.GplotMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.MalformedURLException;

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
        String art_history_data = "http://10.15.191.100:36955/hessian/Gplot.hessian";
        GplotMapper mapper = (GplotMapper) new HessianProxyFactory().create(GplotMapper.class, art_history_data);
        Gplot gplot = mapper.getById(1);
        System.out.println(gplot);

    }
}