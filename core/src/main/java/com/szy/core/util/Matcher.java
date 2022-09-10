package com.szy.core.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/*
of函数生成函数

def gen_of(kv_pair_len):
    arg_str = ", ".join([f"K k{i}, V v{i}" for i in range(1, kv_pair_len + 1)])
    body_str = "\n".join([f"matcher.register(k{i}, v{i});" for i in range(1, kv_pair_len + 1)])
    return"public static <K, V> Matcher<K, V> of(" \
    + arg_str \
    + "){Matcher<K, V> matcher = new Matcher<>();\n" \
    + body_str \
    + " return matcher;}"



[print(gen_of(i)) for i in range(1, 13)]
 */
public class Matcher<K, V> {

    private final Map<K, V> map = new HashMap<>();

    private Matcher() {
    }

    private void register(K k, V v) {
        map.put(k, v);
    }

    private void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    public <V2> Matcher<K, V2> decorateWith(Function<V, V2> decorator) {
        Matcher<K, V2> newMatcher = new Matcher<>();
        forEach((k, v) -> newMatcher.register(k, decorator.apply(v)));
        return newMatcher;
    }

    public V match(K k) {
        return this.map.get(k);
    }

    public boolean containsItem(K k) {
        return this.map.containsKey(k);
    }

    public Matcher<K, V> add(K k, V v) {
        register(k, v);
        return this;
    }

    public Matcher<K, V> add(List<K> ks, V v) {
        ks.forEach(k -> register(k, v));
        return this;
    }

    public Matcher<K, V> addAll(Matcher<K, V> matcher) {
        matcher.forEach(this::register);
        return this;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        matcher.register(k4, v4);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        matcher.register(k4, v4);
        matcher.register(k5, v5);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        matcher.register(k4, v4);
        matcher.register(k5, v5);
        matcher.register(k6, v6);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        matcher.register(k4, v4);
        matcher.register(k5, v5);
        matcher.register(k6, v6);
        matcher.register(k7, v7);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        matcher.register(k4, v4);
        matcher.register(k5, v5);
        matcher.register(k6, v6);
        matcher.register(k7, v7);
        matcher.register(k8, v8);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        matcher.register(k4, v4);
        matcher.register(k5, v5);
        matcher.register(k6, v6);
        matcher.register(k7, v7);
        matcher.register(k8, v8);
        matcher.register(k9, v9);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        matcher.register(k4, v4);
        matcher.register(k5, v5);
        matcher.register(k6, v6);
        matcher.register(k7, v7);
        matcher.register(k8, v8);
        matcher.register(k9, v9);
        matcher.register(k10, v10);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        matcher.register(k4, v4);
        matcher.register(k5, v5);
        matcher.register(k6, v6);
        matcher.register(k7, v7);
        matcher.register(k8, v8);
        matcher.register(k9, v9);
        matcher.register(k10, v10);
        matcher.register(k11, v11);
        return matcher;
    }

    public static <K, V> Matcher<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12) {
        Matcher<K, V> matcher = new Matcher<>();
        matcher.register(k1, v1);
        matcher.register(k2, v2);
        matcher.register(k3, v3);
        matcher.register(k4, v4);
        matcher.register(k5, v5);
        matcher.register(k6, v6);
        matcher.register(k7, v7);
        matcher.register(k8, v8);
        matcher.register(k9, v9);
        matcher.register(k10, v10);
        matcher.register(k11, v11);
        matcher.register(k12, v12);
        return matcher;
    }

}
