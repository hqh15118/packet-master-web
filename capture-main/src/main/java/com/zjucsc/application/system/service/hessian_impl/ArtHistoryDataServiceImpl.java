package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.ArtHistoryData;
import com.zjucsc.application.domain.bean.ArtHistoryForFront;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IArtConfigService;
import com.zjucsc.application.system.service.hessian_iservice.IArtHistoryDataService;
import com.zjucsc.application.system.service.hessian_mapper.ArtHistoryDataMapper;
import com.zjucsc.application.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;


@Service
public class ArtHistoryDataServiceImpl extends BaseServiceImpl<ArtHistoryData,ArtHistoryDataMapper> implements IArtHistoryDataService {

    @Autowired private IArtConfigService iArtConfigService;

    @Override
    public void saveArtData(String artName, String artValue, byte[] payload) {
        this.baseMapper.saveArtData(artName, artValue, payload,Common.GPLOT_ID);
    }

    private HashMap<Integer,Integer> T2TMap = new HashMap<Integer,Integer>(){
        {
            put(5,Calendar.MONTH);
            put(4,Calendar.DAY_OF_MONTH);
            put(3,Calendar.HOUR_OF_DAY);
            put(2,Calendar.MINUTE);
            put(1,Calendar.SECOND);
        }
    };

    private final static HashMap<Integer,List<String>> TIME_LIST =
            new HashMap<Integer,List<String>>(){
                {
                    put(5,Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12"));
                    put(4,Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12",
                            "13","14","15","16","17","18","19","20","21","22","23","24","25","26"
                    ,"27","28","29","30","31"));
                    put(3,Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12",
                            "13","14","15","16","17","18","19","20","21","22","23","24"));
                }
            };

    static {
        List<String> timeList = new ArrayList<>(60);
        for (int i = 1; i < 60; i++) {
            timeList.add(String.valueOf(i));
        }
        TIME_LIST.put(2,timeList);
        TIME_LIST.put(1,timeList);
    }

    @Async
    @Override
    public CompletableFuture<ArtHistoryForFront> getArtData(String startTime, String endTime, List<String> artNames, int timeType) throws Exception {
        ArtHistoryForFront artHistoryForFront = new ArtHistoryForFront();
        artHistoryForFront.setNameList(TIME_LIST.get(timeType));
        Map<String, Map<String,List<String>>> map = new HashMap<>();
        artHistoryForFront.setDataOrz(map);
        artHistoryForFront.setNameList(iArtConfigService.selectAllShowArt());

        int nextTimeType = T2TMap.get(timeType);
        if (nextTimeType < 0){
            throw new Exception("时间类型不可用 ： " + timeType);
        }
        Date startDate = new Date(Long.parseLong(startTime));
        Date endDate = new Date(Long.parseLong(endTime));
        List<Date> dates = DateUtil.getBetweenDates(startDate,endDate,nextTimeType);
        String start , end;

        for (int i = 0 , len = dates.size(); i < len - 1; i++) {
            start = dates.get(i).toString();
            end = dates.get(i + 1).toString();
            //该时间节点下的所有工艺参数
            for (String artName : artNames) {
                Map<String,List<String>> var = map.putIfAbsent(start,new HashMap<>());//[2019-05-25,{var:list<value>}]
                List<String> artHistoryData = this.baseMapper.getArtData(start,end,artName,timeType, Common.GPLOT_ID);
                assert var!=null && artHistoryData!=null;
                var.put(artName,artHistoryData);
            }
        }
        return CompletableFuture.completedFuture(artHistoryForFront);
    }


}
