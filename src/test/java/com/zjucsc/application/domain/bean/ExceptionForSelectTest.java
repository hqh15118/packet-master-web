package com.zjucsc.application.domain.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ExceptionForSelectTest {
    @Test
    public void beanTest(){
        ExceptionForSelect exceptionForSelect = new ExceptionForSelect();
        exceptionForSelect.setCount(100);
        List<ExceptionForSelect.SavedExceptionPacket> savedPackets = new ArrayList<>();
        ExceptionForSelect.SavedExceptionPacket savedPacket = new ExceptionForSelect.SavedExceptionPacket();
        savedPacket.setDstIp("");
        savedPacket.setDstMac("");
        savedPacket.setDstPort("");

        savedPacket.setSrcIp("");
        savedPacket.setSrcMac("");
        savedPacket.setSrcPort("");

        savedPacket.setFuncode(100);
        savedPacket.setTimeStamp("");
        savedPacket.setProtocolName("");

        savedPacket.setBadType(10);
        savedPacket.setDanger(1);
        savedPackets.add(savedPacket);


        exceptionForSelect.setSavedPackets(savedPackets);
        System.out.println(JSON.toJSONString(exceptionForSelect, SerializerFeature.PrettyFormat));

        StatisticInfo statisticInfo = new StatisticInfo();
        statisticInfo.setAttack(Arrays.asList(10,20));
        statisticInfo.setDownload(Arrays.asList(10,20));
        statisticInfo.setException(Arrays.asList(10,20));
        statisticInfo.setUpload(Arrays.asList(10,20));
        statisticInfo.setTimeList(Arrays.asList("time1","time2"));

        System.out.println(JSON.toJSONString(statisticInfo, SerializerFeature.PrettyFormat));
    }
}