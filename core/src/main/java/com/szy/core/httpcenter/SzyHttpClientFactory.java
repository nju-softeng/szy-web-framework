package com.szy.core.httpcenter;


import org.springframework.stereotype.Component;

/**
 * @author zzsnowy
 */
@Component
public interface SzyHttpClientFactory {

    SzyHttpClient getClient();

}
