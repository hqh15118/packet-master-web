package com.zjucsc.application.system.service.hessian_impl;

import com.zjucsc.application.config.Common;
import com.zjucsc.application.domain.bean.OptFilterForFront;
import com.zjucsc.application.domain.exceptions.DeviceNotValidException;
import com.zjucsc.application.domain.exceptions.OptFilterNotValidException;
import com.zjucsc.application.domain.exceptions.ProtocolIdNotValidException;
import com.zjucsc.application.domain.bean.OptFilter;
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
import java.util.List;
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
     */
    @Async
    @Override
    public CompletableFuture<Exception> addOperationFilter(OptFilterForFront optFilterForFront) throws DeviceNotValidException {
        String deviceIp = CommonCacheUtil.getTargetDeviceIpByNumber(optFilterForFront.getDeviceNumber());
        if (deviceIp == null){
            throw new DeviceNotValidException("未发现设备号为["+optFilterForFront.getDeviceNumber() +"]的设备");
        }
        try {
            //更新缓存
            CommonOptFilterUtil.addOrUpdateAnalyzer(deviceIp,optFilterForFront,optFilterForFront.toString());
        } catch (ProtocolIdNotValidException e) {
            return CompletableFuture.completedFuture(e);
        }
        //更新数据库
        this.baseMapper.saveOrUpdateBatch(optFilterForFront,Common.GPLOT_ID);
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 获取某个设备针对某个协议的功能码配置
     * @param deviceNumber 设备
     * @param cached 是否从缓存里面找
     * @param protocolId 协议
     * @return
     * @throws ProtocolIdNotValidException
     */
    @Async
    @Override
    public CompletableFuture<List<Integer>> getTargetExistIdFilter(String deviceNumber, boolean cached , int protocolId) throws ProtocolIdNotValidException {
        if (cached){
            String deviceIp = CommonCacheUtil.getTargetDeviceIpByNumber(deviceNumber);
            ConcurrentHashMap<String, OperationAnalyzer> map = Common.OPERATION_FILTER_PRO.get(deviceIp);
            if (map == null){
                throw new ProtocolIdNotValidException("缓存中不存在ID为 " + deviceNumber + " 的规则");
            }
            final List<Integer> optFilterList  = new ArrayList<>();
            OperationAnalyzer analyzer = map.get(convertIdToName(protocolId));
//                if (type == 0) {
//                    analyzer.getAnalyzer().getWhiteMap().forEach(new BiConsumer<Integer, String>() {
//                        @Override
//                        public void accept(Integer integer, String s) {
//                            optFilterList.add(integer);
//                        }
//                    });
//                } else {
//                    analyzer.getAnalyzer().getBlackMap().forEach(new BiConsumer<Integer, String>() {
//                        @Override
//                        public void accept(Integer integer, String s) {
//                            optFilterList.add(integer);
//                        }
//                    });
//                }
            if (analyzer == null || analyzer.getAnalyzer() == null || analyzer.getAnalyzer().getWhiteMap() == null){
                return CompletableFuture.completedFuture(new ArrayList<>(0));
            }
            analyzer.getAnalyzer().getWhiteMap().forEach(new BiConsumer<Integer, String>() {
                @Override
                public void accept(Integer integer, String s) {
                    optFilterList.add(integer);
                }
            });
            return CompletableFuture.completedFuture(optFilterList);
        }else{
            return CompletableFuture.completedFuture(selectTargetOptFilter(deviceNumber  , protocolId));
        }
    }

    @Override
    public List<Integer> selectTargetOptFilter(String device, int protocolId) {
        return this.baseMapper.selectTargetOptFilter(device,protocolId,Common.GPLOT_ID);
    }

    @Override
    public void deleteByDeviceNumber(String deviceNumber) {
        this.baseMapper.deleteByDeviceNumber(deviceNumber,Common.GPLOT_ID);
    }


    @Override
    public void deleteByDeviceNumberAndProtocolId(String deviceNumber, int protocolId) {
        this.baseMapper.deleteByDeviceNumberAndProtocolId(deviceNumber, protocolId,Common.GPLOT_ID);
    }

    @Override
    public void deleteByDeviceNumberAndProtocolIdAndFuncode(String deviceNumber, int protocolId, int funCode) {
        this.baseMapper.deleteByDeviceNumberAndProtocolIdAndFuncode(deviceNumber, protocolId, funCode,Common.GPLOT_ID);
    }
}
