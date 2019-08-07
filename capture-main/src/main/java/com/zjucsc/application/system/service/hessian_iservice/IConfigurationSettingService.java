package com.zjucsc.application.system.service.hessian_iservice;

import com.zjucsc.application.domain.bean.ConfigurationForSelect;
import com.zjucsc.application.domain.bean.ConfigurationSetting;

import java.util.List;

/**
 * @author hongqianhui
 */
public interface IConfigurationSettingService {
    //请求已配置好的或者数据库中的组态规则
    //CompletableFuture<HashMap<String, Object>> loadRuleAll(int deviceId) throws ExecutionException, InterruptedException, DeviceNotValidException;
    //CompletableFuture<Exception> addNewConfiguration(List<String> )


    List<ConfigurationSetting> selectConfigurationInfo(ConfigurationForSelect configurationForSelect);

    int selectConfigurationCount(String codeDes, int protocolId);

    void updateFuncode(int protocolId, String fun_code, String opt);

    List<ConfigurationSetting> selectAll();

    void saveOrUpdateBatch(List<ConfigurationSetting> configurationSettings);

    void deleteByProtocolIdAndFuncode(int protocolId, String funCode);

    void deleteByProtocolId(int protocolId);
}
