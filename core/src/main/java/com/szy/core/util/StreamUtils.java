package com.szy.core.util;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamUtils {

    public static <T, T1> T1 foldLeft(Collection<T> collection, T1 acc, BiFunction<T1, T, T1> biFunction) {
        if (collection == null) {
            return null;
        }
        for (T item : collection) {
            acc = biFunction.apply(acc, item);
        }
        return acc;
    }

    public static <T, R> List<R> listMapping(List<T> list, Function<? super T, ? extends R> mapper) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(mapper).collect(Collectors.toList());
    }


}
