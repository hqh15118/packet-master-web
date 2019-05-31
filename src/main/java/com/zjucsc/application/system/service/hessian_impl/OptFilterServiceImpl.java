package com.zjucsc.application.system.service.hessian_impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.OptFilterNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.system.entity.OptFilter;
import com.zjucsc.application.system.mapper.base.BaseServiceImpl;
import com.zjucsc.application.system.service.hessian_iservice.IOptFilterService;
import com.zjucsc.application.system.service.hessian_mapper.OptFilterMapper;
import com.zjucsc.application.tshark.analyzer.OperationAnalyzer;
import com.zjucsc.application.tshark.filter.OperationPacketFilter;
import com.zjucsc.application.util.CommonCacheUtil;
import com.zjucsc.application.util.CommonConfigUtil;
import com.zjucsc.application.util.CommonOptFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import static com.zjucsc.application.util.CommonCacheUtil.convertIdToName;

/**
 * @author hongqianhui
 */
@Slf4j
@Service
public class OptFilterServiceImpl extends BaseServiceImpl<OptFilterMapper, OptFilter> implements IOptFilterService {

    /**
     * 每次传的时候，都传功能码那里所有协议下的所有配置
     * 会将该设备ID下的map[协议，分析器]整个替换掉。
     * @param
     * @return
     * @throws ProtocolIdNotValidException
     */
    @Async
    @Override
    public CompletableFuture<Exception> addOperationFilter(OptFilterForFront optFilterForFront) throws ProtocolIdNotValidException, OptFilterNotValidException {
        StringBuilder sb = new StringBuilder();
        String deviceNumber = optFilterForFront.getDeviceNumber();
        String deviceIp = CommonCacheUtil.getTargetDeviceIpByNumber(deviceNumber);
        if (deviceIp == null){
            log.error("异常，设备[deviceNumber] :  {} 对应的IP地址未添加到缓存中");
            return CompletableFuture.completedFuture(new DeviceNotValidException("未发现 " + optFilterForFront.getDeviceNumber() +" 的设备IP"));
        }
        String userName = optFilterForFront.getUserName();
        int protocolId = optFilterForFront.getProtocolId();

        this.baseMapper.deleteByDeviceNumber(deviceNumber);
        //ConcurrentHashMap<String,OperationAnalyzer> analyzerMap = new ConcurrentHashMap<>(); //每一个设备都需要这样一个map，key是协议类型，value是该协议下对应的分析器
        List<OptFilter> optFilters = new ArrayList<>();
        OperationPacketFilter<Integer,String> operationPacketFilter = new OperationPacketFilter<>
                (sb.append(deviceNumber).append(" : ").append(userName).append(convertIdToName(protocolId)).toString());//该协议对应的分析器的报文过滤器
        for (OptFilterForFront.IOptFilter iOptFilter : optFilterForFront.getIOptFilters()) {
            String protocol = convertIdToName(protocolId);
            //实例化数据库实体
            OptFilter optFilter = new OptFilter();
            optFilter.setFilterType(iOptFilter.getFilterType());
            optFilter.setDeviceNumber(deviceNumber);
            optFilter.setUserName(userName);
            optFilter.setProtocolId(protocolId);
            optFilter.setFunCode(iOptFilter.getFunCode());
            optFilter.setGplotId(Common.GPLOT_ID);
            optFilters.add(optFilter);
            //更新该协议下的过滤器
            if (iOptFilter.getFilterType() == 0){
                //白名单
                operationPacketFilter.addWhiteRule(iOptFilter.getFunCode(),
                        CommonConfigUtil.getTargetProtocolFuncodeMeanning(protocol , iOptFilter.getFunCode()));//添加功能码以及该功能码对应的含义
            }else{
                //黑名单
                operationPacketFilter.addBlackRule(iOptFilter.getFunCode(),
                        CommonConfigUtil.getTargetProtocolFuncodeMeanning(protocol , iOptFilter.getFunCode()));//添加功能码以及该功能码对应的含义
            }
        }
        //analyzerMap.put(convertIdToName(protocolId) , new OperationAnalyzer(operationPacketFilter));
        this.baseMapper.saveBatch(optFilters);                              //将新的过滤规则保存到数据库
        CommonOptFilterUtil.addOrUpdateAnalyzer(deviceIp,convertIdToName(protocolId),new OperationAnalyzer(operationPacketFilter));  //替换旧的过滤规则
        log.info("addOperationFilter [device number :  {} ; device ip {} ;protocol id : {} protocol name {} ] : new operation filter of {} \n" +
                        "and OPERATION_FILTER is {} " ,
                deviceNumber , deviceIp , protocolId , convertIdToName(protocolId) , operationPacketFilter , Common.OPERATION_FILTER_PRO);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<List<Integer>> getTargetExistIdFilter(String deviceId, int type , boolean cached , int protocolId) throws ProtocolIdNotValidException {
        if (cached){
            String deviceIp = CommonCacheUtil.getTargetDeviceIpByNumber(deviceId);
            ConcurrentHashMap<String, OperationAnalyzer> map = Common.OPERATION_FILTER_PRO.get(deviceIp);
            if (map == null){
                throw new ProtocolIdNotValidException("缓存中不存在ID为 " + deviceId + " 的规则");
            }
            final List<Integer> optFilterList  = new ArrayList<>();
            OperationAnalyzer analyzer = map.get(convertIdToName(protocolId));
            try {
                if (type == 0) {
                    analyzer.getAnalyzer().getWhiteMap().forEach(new BiConsumer<Integer, String>() {
                        @Override
                        public void accept(Integer integer, String s) {
                            optFilterList.add(integer);
                        }
                    });
                } else {
                    analyzer.getAnalyzer().getBlackMap().forEach(new BiConsumer<Integer, String>() {
                        @Override
                        public void accept(Integer integer, String s) {
                            optFilterList.add(integer);
                        }
                    });
                }
            }catch (NullPointerException e){
                return null;
            }
            return CompletableFuture.completedFuture(optFilterList);
        }else{
            return CompletableFuture.completedFuture(selectTargetOptFilter(deviceId , type , protocolId));
        }
    }

    @Override
    public CompletableFuture<Exception> deleteTargetDeviceFilters(int deviceId) {
        return null;
    }

    @Override
    public List<Integer> selectTargetOptFilter(String device, int type, int protocolId) {
        return this.baseMapper.selectTargetOptFilter(device,type,protocolId);
    }
}
