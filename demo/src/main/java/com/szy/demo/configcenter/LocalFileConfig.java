package com.szy.demo.configcenter;

import com.szy.core.configcenter.annotation.SConfigCenter;
import com.szy.core.configcenter.annotation.SValue;
import com.szy.demo.configcenter.dto.SUserConfig;

@SConfigCenter
public interface LocalFileConfig {

    @SValue("llj")
    Long getFixedValue();

    SUserConfig getUserValue();

}
