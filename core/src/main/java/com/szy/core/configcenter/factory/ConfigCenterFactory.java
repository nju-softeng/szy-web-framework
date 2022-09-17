package com.szy.core.configcenter.factory;

import com.szy.core.configcenter.ConfigFetcher;
import com.szy.core.configcenter.annotation.SValue;
import com.szy.core.util.GenericBuilder;
import com.szy.core.util.TypeUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.CallbackHelper;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Component
public class ConfigCenterFactory<T> implements FactoryBean<T> {

    private final Class<T> interfaceType;

    @Resource
    ConfigFetcher configFetcher;

    private final InvocationHandler invocationHandler = (o, method1, objects) -> {
        Class<?> returnType = method1.getReturnType();
        return buildConfigObj(returnType, method1.getAnnotation(SValue.class));
    };

    /**
     * 由 beanDefinition 注入
     */
    public ConfigCenterFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    /**
     * 所有的检查都交给对应的processor
     * @return 代理bean
     */
    @Override
    public T getObject() {
        final Class<?> superClass = Object.class;
        final Class<?>[] interfaces = new Class[]{interfaceType};
        CallbackHelper callbackHelper = new CallbackHelper(superClass, interfaces) {
            @Override
            protected Object getCallback(Method method) {
                return method.isDefault()
                        ? NoOp.INSTANCE
                        : invocationHandler;
            }
        };

        Enhancer enhancer = GenericBuilder.of(Enhancer::new)
                .with(Enhancer::setSuperclass, superClass)
                .with(Enhancer::setInterfaces, interfaces)
                .with(Enhancer::setCallbackFilter, callbackHelper)
                .with(Enhancer::setCallbackTypes, callbackHelper.getCallbackTypes())
                .with(Enhancer::setCallbacks, callbackHelper.getCallbacks())
                .build();

        return (T) enhancer.create();
    }


    /**
     * 构造一个这个类的对象
     */
    private Object buildConfigObj(Class<?> clazz, @Nullable SValue sValue) throws InstantiationException, IllegalAccessException {
        if (TypeUtils.isConfigBasicTypes(clazz)) {
            return TypeUtils.transformToBasicType(configFetcher.find(sValue.value()), clazz);
        }
        Object ret = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            SValue annotation = field.getAnnotation(SValue.class);
            boolean isAccess = field.isAccessible();
            field.setAccessible(true);
            field.set(ret, buildConfigObj(field.getType(), annotation));
            field.setAccessible(isAccess);
        }
        return ret;
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
