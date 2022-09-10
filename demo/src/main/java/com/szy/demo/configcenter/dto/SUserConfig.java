package com.szy.demo.configcenter.dto;

import com.szy.core.configcenter.annotation.SValue;
import lombok.Data;

@Data
public class SUserConfig {
    @SValue("susername")
    private String username;
    @SValue("spassword")
    private String password;
}
