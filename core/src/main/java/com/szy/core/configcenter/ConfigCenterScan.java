package com.szy.core.configcenter;

import com.szy.core.configcenter.impl.DefaultConfigFetcher;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 项目依赖时，@Import中标识的类会注册到容器中
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ConfigCenterProcessor.class, DefaultConfigFetcher.class})
public @interface ConfigCenterScan {
    String value();
}
