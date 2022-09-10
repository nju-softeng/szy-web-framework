package com.szy.core.util;

import java.util.function.Supplier;

public class GenericBuilder<T> {
    private T obj;

    private GenericBuilder(T obj) {
        this.obj = obj;
    }

    public static <T> GenericBuilder<T> of(Supplier<T> supplier) {
        return new GenericBuilder<>(supplier.get());
    }

    public T build() {
        return obj;
    }

    public <T1> GenericBuilder<T> with(Consumer1<T, T1> consumer1, T1 value) {
        consumer1.accept(obj, value);
        return this;
    }

    public <T1, T2> GenericBuilder<T> with(Consumer2<T, T1, T2> consumer2, T1 value1, T2 value2) {
        consumer2.accept(obj, value1, value2);
        return this;
    }

    public <T1, T2, T3> GenericBuilder<T> with(Consumer3<T, T1, T2, T3> consumer3, T1 value1, T2 value2, T3 value3) {
        consumer3.accept(obj, value1, value2, value3);
        return this;
    }

    public <T1, T2, T3, T4> GenericBuilder<T> with(Consumer4<T, T1, T2, T3, T4> consumer4, T1 value1, T2 value2, T3 value3, T4 value4) {
        consumer4.accept(obj, value1, value2, value3, value4);
        return this;
    }

    @FunctionalInterface
    public interface Consumer1<T, T1> {
        void accept(T obj, T1 arg1);
    }

    @FunctionalInterface
    public interface Consumer2<T, T1, T2> {
        void accept(T obj, T1 arg1, T2 arg2);
    }

    @FunctionalInterface
    public interface Consumer3<T, T1, T2, T3> {
        void accept(T obj, T1 arg1, T2 arg2, T3 arg3);
    }

    @FunctionalInterface
    public interface Consumer4<T, T1, T2, T3, T4> {
        void accept(T obj, T1 method1, T2 arg2, T3 arg3, T4 arg4);
    }


}
