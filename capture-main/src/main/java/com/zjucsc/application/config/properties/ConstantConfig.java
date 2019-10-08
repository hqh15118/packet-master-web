package com.zjucsc.application.config.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * #project packet-master-web
 *
 * @author hongqianhui
 * #create_time 2019-05-14 - 22:51
 */

@ConfigurationProperties(prefix = "myconfig")
@Data
public class ConstantConfig {

    private String global_address;

    @Value("#{systemProperties['os.name']}")
    private String systemPropertiesName; // 注入操作系统属性

    private boolean openAOPLog;

    private boolean sendAopLog;

    private boolean openMapperAop;

    private boolean showErrorOnly;

    private int reOpenTableName;

    private int simulateStartNum;

    private String tshark_path;

    private String temp_path;

    private String max_temp_file_size;

    private String wireshark_version;
}
