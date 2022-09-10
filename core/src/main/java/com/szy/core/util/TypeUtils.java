package com.szy.core.util;

import com.google.common.collect.ImmutableSet;

import java.util.function.Function;

public class TypeUtils {
    private final static Matcher<Class, Function<String, Object>>
            CONFIG_CENTER_BASIC_TYPE_CAST_LOGIC_MATCHER = Matcher.of(
            String.class, val -> val,
            Integer.class, Integer::valueOf,
            Long.class, Long::valueOf,
            Float.class, Float::valueOf,
            Double.class, Double::valueOf,
            int.class, Integer::valueOf,
            long.class, Long::valueOf,
            float.class, Float::valueOf,
            double.class, Double::valueOf
    );

    public static boolean isConfigBasicTypes(Class clazz) {
        return CONFIG_CENTER_BASIC_TYPE_CAST_LOGIC_MATCHER.containsItem(clazz);
    }

    public static <T> T transformToBasicType(String configValue, Class<T> clazz) {
        return (T) CONFIG_CENTER_BASIC_TYPE_CAST_LOGIC_MATCHER.match(clazz).apply(configValue);
    }
}
