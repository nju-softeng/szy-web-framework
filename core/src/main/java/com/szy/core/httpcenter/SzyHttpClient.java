package com.szy.core.httpcenter;

import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zzsnowy
 */
@Component
public interface SzyHttpClient {

    SzyHttpClient setUrl(String url);
    SzyHttpClient setHttpMethod(String methodName);
    SzyHttpClient setHeader(String key, String value);
    <T> SzyHttpClient setBody(T t);
    <R> R excute(Type type);
}
