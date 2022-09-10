package com.szy.demo.controller.impl;

import com.szy.demo.configcenter.dto.SUserConfig;
import com.szy.demo.configcenter.LocalFileConfig;
import com.szy.demo.controller.ConfigController;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ConfigControllerImpl implements ConfigController {

    @Resource
    LocalFileConfig localFileConfig;

    @Override
    public Long getFixedValueConfig() {
        return localFileConfig.getFixedValue();
    }

    @Override
    public SUserConfig getObjConfig() {
        return localFileConfig.getUserValue();
    }


}
