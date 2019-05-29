package com.zjucsc.application.system.controller;

import com.zjucsc.application.system.service.iservice.IArtHistoryData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ArtHistoryDataControllerTest {

    @Autowired
    private IArtHistoryData iArtHistoryData;

    @Test
    public void getArtHistoryData() {
        iArtHistoryData.saveArtData("test_art_name",1.0F,new byte[]{1,2,3});
    }
}