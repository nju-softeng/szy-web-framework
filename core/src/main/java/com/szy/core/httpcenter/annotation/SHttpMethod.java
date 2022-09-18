package com.szy.core.httpcenter.annotation;

import java.lang.annotation.*;

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SHttpMethod {
    String value();
}
