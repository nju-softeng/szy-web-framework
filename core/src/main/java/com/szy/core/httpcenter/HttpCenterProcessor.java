package com.szy.core.httpcenter;

import com.szy.core.httpcenter.annotation.HttpCenterScan;
import com.szy.core.httpcenter.annotation.SHttpCenter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@Slf4j
public class HttpCenterProcessor implements
        ImportBeanDefinitionRegistrar,
        ResourceLoaderAware,
        EnvironmentAware {

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private MetadataReaderFactory metadataReaderFactory;

    private ResourcePatternResolver resourcePatternResolver;

    private Environment environment;

    @SneakyThrows
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        AnnotationAttributes httpScanAttr = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(HttpCenterScan.class.getName()));
        String basePackageName = httpScanAttr.getString("value");
        Set<Class<?>> beanClazzs = scannerPackages(basePackageName);
        for (Class beanClazz : beanClazzs) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
            GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClazz);
            definition.setBeanClass(HttpCenterFactory.class);

            //这里采用的是byType方式注入，类似的还有byName等
            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
            registry.registerBeanDefinition(beanClazz.getSimpleName(), definition);
        }
    }

    /**
     * 根据包路径获取包及子包下的所有类
     *
     * @param basePackage basePackage
     */
    private Set<Class<?>> scannerPackages(String basePackage) throws ClassNotFoundException, IOException {
        Set<Class<?>> set = new LinkedHashSet<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + '/' + DEFAULT_RESOURCE_PATTERN;
        Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
        for (Resource resource : resources) {
            if (!resource.isReadable()) {
                continue;
            }
            MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
            String className = metadataReader.getClassMetadata().getClassName();
            Class<?> clazz;
            clazz = Class.forName(className);
            Annotation annotation = clazz.getAnnotation(SHttpCenter.class);
            if (annotation == null) {
                continue;
            }
            set.add(clazz);
        }
        return set;
    }

    protected String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
