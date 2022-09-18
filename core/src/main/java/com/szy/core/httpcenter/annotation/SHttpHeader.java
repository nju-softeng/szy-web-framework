package com.szy.core.httpcenter.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(SHttpHeaders.class)
public @interface SHttpHeader {
    String key();
    String value();
}
