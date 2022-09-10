package com.szy.core.configcenter.impl;

import com.szy.core.configcenter.ConfigFetcher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class DefaultConfigFetcher implements ConfigFetcher {

    @Resource
    Environment environment;

    private final ConcurrentMap<String, String> configCache = new ConcurrentHashMap<>();

    @Override
    public String find(String key) {
        return configCache.computeIfAbsent(key, environment::getProperty);
    }

    @Override
    public String refresh(String key) {
        return configCache.computeIfPresent(key, (oldKey, oldValue) -> environment.getProperty(oldKey));
    }
}
