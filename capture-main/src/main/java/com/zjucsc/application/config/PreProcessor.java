package com.zjucsc.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "pre-processors")
@Data
public class PreProcessor {
    private List<String> list;
}
