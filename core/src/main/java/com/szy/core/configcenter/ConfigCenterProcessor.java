package com.szy.core.configcenter;

import com.szy.core.InterfaceProxyProcessor;
import com.szy.core.configcenter.annotation.SConfigCenter;
import com.szy.core.configcenter.factory.ConfigCenterFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConfigCenterProcessor implements
        InterfaceProxyProcessor,
        ImportBeanDefinitionRegistrar,
        ResourceLoaderAware,
        EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @SneakyThrows
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
    public boolean interfaceFilter(Class interfaceType) {
        return interfaceType.getAnnotation(SConfigCenter.class) != null;
    }

    @Override
    public Class getFactoryBeanClass() {
        return ConfigCenterFactory.class;
    }


    @Override
    public Class getPackageAnnotation() {
        return ConfigCenterScan.class;
    }
}
