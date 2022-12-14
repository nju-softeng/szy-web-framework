package com.szy.core.configcenter;

import com.szy.core.InterfaceProxyProcessor;
import com.szy.core.configcenter.annotation.SConfigCenter;
import com.szy.core.configcenter.annotation.SValue;
import com.szy.core.configcenter.factory.ConfigCenterFactory;
import com.szy.core.eunm.SzyExceptionEnum;
import com.szy.core.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class ConfigCenterProcessor implements
        InterfaceProxyProcessor,
        ImportBeanDefinitionRegistrar,
        ResourceLoaderAware,
        EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerSzyBeanDefinitions(importingClassMetadata, registry, resourceLoader, environment);
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean interfaceFilter(Class<?> interfaceType) {
        return interfaceType.getAnnotation(SConfigCenter.class) != null;
    }

    @Override
    public Class<?> getFactoryBeanClass() {
        return ConfigCenterFactory.class;
    }


    @Override
    public Class<?> getPackageAnnotation() {
        return ConfigCenterScan.class;
    }

    @Override
    public void preCheck(Class<?> interfaceType) {
        // 1. ???????????????????????????
        SzyExceptionEnum.CONFIG_CENTER_ANNOTATION_NOT_FOUND
                .throwsIf(interfaceType.getAnnotation(SConfigCenter.class) == null);
        // 2. ???default????????????
        Arrays.stream(interfaceType.getMethods())
                .filter(method -> !method.isDefault())
                .forEach(method -> {
                    // 2.1 ??????????????????????????????????????????
                    SzyExceptionEnum.CONFIG_CENTER_METHOD_SIGNATURE_ERROR
                            .throwsIf(method.getParameterCount() != 0 || method.getReturnType() == void.class);
                    // 2.2 ?????????????????????
                    checkReturnType(method.getReturnType(), method.getAnnotation(SValue.class));
                });
    }

    private void checkReturnType(Class<?> returnType, SValue sValue) {
        // 1. ?????????????????????@SValue??????
        if (TypeUtils.isConfigBasicTypes(returnType)) {
            SzyExceptionEnum.CONFIG_CENTER_ANNOTATION_NOT_FOUND.throwsIf(sValue == null);
            return;
        }
        // 2. ?????????????????????field????????????????????????
        Arrays.stream(returnType.getDeclaredFields())
                .forEach(field -> checkReturnType(field.getType(), field.getAnnotation(SValue.class)));
    }
}
