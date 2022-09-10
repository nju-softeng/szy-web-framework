package com.szy.core.configcenter;

import java.util.Arrays;

public interface ConfigFetcher {

    String find(String key);

    default String[] batchFind(String... keys) {
        return Arrays.stream(keys)
                .map(this::find)
                .toArray(String[]::new);
    }

    String refresh(String key);

    default String[] batchRefresh(String... keys) {
        return Arrays.stream(keys)
                .map(this::refresh)
                .toArray(String[]::new);
    }

    default void refreshAll() {
        return;
    }

}
