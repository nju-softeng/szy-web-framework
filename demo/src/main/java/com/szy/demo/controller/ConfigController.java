package com.szy.demo.controller;

import com.szy.demo.configcenter.dto.SUserConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/config")
public interface ConfigController {

    @GetMapping("/fixed")
    Long getFixedValueConfig();

    @GetMapping("/obj")
    SUserConfig getObjConfig();

}
