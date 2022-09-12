package com.szy.core.httpcenter.annotation;

import com.szy.core.httpcenter.HttpCenterProcessor;
import com.szy.core.httpcenter.impl.DefaultSzyHttpClientFactory;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({HttpCenterProcessor.class, DefaultSzyHttpClientFactory.class})
public @interface HttpCenterScan {
    String value();
}
