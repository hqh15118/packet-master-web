package com.zjucsc.application.system.service.hessian_impl;


import com.zjucsc.application.system.service.hessian_iservice.IArtHistoryDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArtHistoryDataServiceImplTest {

    @Autowired private IArtHistoryDataService iArtHistoryDataService;

    @Test
    public void saveDataTest(){
        String artName = "test_arg";
        String value = "1.0";
        iArtHistoryDataService.saveArtData(artName,value,new byte[1]);
    }
}