package com.szy.core.httpcenter.impl;

import com.szy.core.httpcenter.SzyHttpClient;
import com.szy.core.httpcenter.SzyHttpMethod;
import com.szy.core.util.Matcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zzsnowy
 */
@Component
@Slf4j
public class DefaultSzyHttpClient implements SzyHttpClient {

    RestTemplate restTemplate;
    String url;
    HttpMethod httpMethod;
    HttpHeaders headers;

    Map<String, ?> requestParams;
    HttpEntity<?> request;

    public DefaultSzyHttpClient(){
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
    }
    @Override
    public SzyHttpClient setUrl(String url) {
        this.url = url;
        return this;
    }

    Matcher<String, HttpMethod> METHOD_MATCHER = Matcher.of(
            SzyHttpMethod.POST, HttpMethod.POST,
            SzyHttpMethod.GET, HttpMethod.GET,
            SzyHttpMethod.DELETE, HttpMethod.DELETE,
            SzyHttpMethod.HEAD, HttpMethod.HEAD,
            SzyHttpMethod.PUT, HttpMethod.PUT,
            SzyHttpMethod.PATCH, HttpMethod.PATCH,
            SzyHttpMethod.OPTIONS, HttpMethod.OPTIONS,
            SzyHttpMethod.TRACE, HttpMethod.TRACE
    );

    @Override
    public SzyHttpClient setHttpMethod(String methodName) {
        this.httpMethod = METHOD_MATCHER.match(methodName);
        return this;
    }

    @Override
    public SzyHttpClient setHeader(String key, String value) {
        headers.add(key, value);
        return this;
    }

    @Override
    public SzyHttpClient setBody(Object o) {
        request = new HttpEntity<>(o, headers);
        return this;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <R> R excute(Type type) {
        log.info("URL:" + url);
        log.info("request:" + request);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, type);
        ResponseExtractor<ResponseEntity<R>> responseExtractor = restTemplate.responseEntityExtractor(type);
        return (R) ((ResponseEntity<?>)restTemplate.execute(url, httpMethod, requestCallback, responseExtractor)).getBody();
    }

}
