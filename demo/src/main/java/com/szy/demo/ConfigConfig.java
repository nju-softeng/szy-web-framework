package com.szy.demo;

import com.szy.core.configcenter.ConfigFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定制你的配置中心，使用spring配置、http、数据库。。。等获取配置
 */
@Configuration
public class ConfigConfig {

    @Bean
    public ConfigFetcher configFetcher() {
        return new ConfigFetcher() {
            @Override
            public String find(String key) {
                return "1";
            }

            @Override
            public String refresh(String key) {
                return "2";
            }
        };
    }

}
