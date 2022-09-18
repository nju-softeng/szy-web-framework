package com.szy.demo;

import com.szy.core.configcenter.ConfigCenterScan;
import com.szy.core.httpcenter.annotation.HttpCenterScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ConfigCenterScan("com.szy.demo.configcenter")
@HttpCenterScan("com.szy.demo.httpcenter")
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
