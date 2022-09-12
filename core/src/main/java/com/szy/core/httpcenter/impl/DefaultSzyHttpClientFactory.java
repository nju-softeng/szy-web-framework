package com.szy.core.httpcenter.impl;

import com.szy.core.httpcenter.SzyHttpClient;
import com.szy.core.httpcenter.SzyHttpClientFactory;
import org.springframework.stereotype.Component;

/**
 * @author zzsnowy
 */
@Component
public class DefaultSzyHttpClientFactory implements SzyHttpClientFactory {

    @Override
    public SzyHttpClient getClient() {
        return new DefaultSzyHttpClient();
    }
}
