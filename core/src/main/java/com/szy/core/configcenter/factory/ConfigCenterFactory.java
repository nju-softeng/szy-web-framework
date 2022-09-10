package com.szy.core.configcenter.factory;

import com.szy.core.configcenter.ConfigFetcher;
import com.szy.core.configcenter.annotation.SValue;
import com.szy.core.eunm.SzyExceptionEnum;
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

    @Override
    public T getObject() throws Exception {
        Method[] methods = interfaceType.getMethods();
        for (Method method : methods) {
            if (method.isDefault()) {
                continue;
            }
            SzyExceptionEnum.CONFIG_CENTER_METHOD_SIGNATURE_ERROR
                    .throwsIf(method.getParameterCount() != 0 || method.getReturnType() == void.class);
            SzyExceptionEnum.CONFIG_CENTER_ANNOTATION_NOT_FOUND
                    .throwsIf(TypeUtils.isConfigBasicTypes(method.getReturnType()) && method.getAnnotation(SValue.class) == null);
        }

        CallbackHelper callbackHelper = new CallbackHelper(Object.class, new Class[]{interfaceType}) {
            @Override
            protected Object getCallback(Method method) {
                return method.isDefault()
                        ? NoOp.INSTANCE
                        : invocationHandler;
            }
        };

        Enhancer enhancer = GenericBuilder.of(Enhancer::new)
                .with(Enhancer::setSuperclass, Object.class)
                .with(Enhancer::setInterfaces, new Class[] {interfaceType})
                .with(Enhancer::setCallbackFilter, callbackHelper)
                .with(Enhancer::setCallbackTypes, callbackHelper.getCallbackTypes())
                .with(Enhancer::setCallbacks, callbackHelper.getCallbacks())
                .build();

        return (T) enhancer.create();
    }


    /**
     * 构造一个这个类的对象
     * 填充他的成员变量，成员变量要么为(基本类型 && SValue修饰)
     * 要么为非基本类型
     * @param clazz
     */
    private Object buildConfigObj(Class<?> clazz, @Nullable SValue sValue) throws InstantiationException, IllegalAccessException {
        if (TypeUtils.isConfigBasicTypes(clazz)) {
            SzyExceptionEnum.CONFIG_CENTER_ANNOTATION_NOT_FOUND.throwsIf(sValue == null);
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
