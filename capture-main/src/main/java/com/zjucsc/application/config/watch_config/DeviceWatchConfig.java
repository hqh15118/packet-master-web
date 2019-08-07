package com.zjucsc.application.config.watch_config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zjucsc.application.domain.bean.Device;
import com.zjucsc.application.util.CommonCacheUtil;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.function.BiConsumer;

@Endpoint(id = "devices")
@Configuration
public class DeviceWatchConfig {

    @ReadOperation
    public Map<String,Object> getConfigs2(){
        HashMap<String,Object> map = new HashMap<>();
        Map<String,String> devices = new HashMap<>();
        CommonCacheUtil.getAllDevices().forEach((deviceMac, device) -> devices.put(deviceMac,device.toString()));
        map.put("DEVICE_NUMBER",devices.size());
        map.put("ALL_DEVICES", JSON.toJSONString(devices, SerializerFeature.PrettyFormat));
        return map;
    }


//    @WriteOperation
    public Device getDeviceInfoByDeviceTag(String deviceTag){
        return CommonCacheUtil.getAllDevices().get(deviceTag);
    }


    @SuppressWarnings("unchecked")
    @WriteOperation
    public List getDeviceInfos(String operation){
        Map<String,Device> allDevice = CommonCacheUtil.getAllDevices();
        List list = new ArrayList();
        switch (operation){
            //获取所有的设备info
            case "0" : allDevice.forEach((deviceTag, device) -> list.add(device.getDeviceInfo()));
            break;
            case "1" : allDevice.forEach((deviceTag, device) -> list.add(device.getDeviceTag()));
            break;
            case "2" : allDevice.forEach((deviceTag, device) -> {
                list.add(device.getCreateTime());
            });
            break;
            default:return Collections.singletonList(getDeviceInfoByDeviceTag(operation));
        }
        return list;
    }
}
