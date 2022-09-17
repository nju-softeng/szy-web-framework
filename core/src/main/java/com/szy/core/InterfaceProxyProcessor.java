package com.szy.core;

import lombok.SneakyThrows;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 接口代理处理器的模版方法类
 */
public interface InterfaceProxyProcessor {

    String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    /**
     * 对要代理的接口的过滤函数
     * @param interfaceType 一个接口
     * @return 是否代理这个接口
     */
    boolean interfaceFilter(Class<?> interfaceType);

    /**
     * 代理这个接口的FactoryBean的类
     * @return FactoryBean实现类的class
     */
    Class<?> getFactoryBeanClass();

    /**
     * 返回提供给用户注册扫描包名的注解
     * @return 某个注解，value为包名
     */
    Class<?> getPackageAnnotation();

    /**
     * 对要代理的接口定义预检，确保符合定义，不符合定义抛异常
     * @param interfaceType 要代理的接口
     */
    void preCheck(Class<?> interfaceType);

    /**
     * 注册bean定义的模版方法，方便复用
     */
    @SneakyThrows
    default void registerSzyBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, ResourceLoader resourceLoader, Environment environment) {

        // 1. 通过提供给用户的扫描包注解获取要扫描的包名
        AnnotationAttributes configScanAttr = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(getPackageAnnotation().getName()));
        assert configScanAttr != null;
        String basePackageName = configScanAttr.getString("value");

        // 2. 扫描包，获取要代理的接口集合
        Set<Class<?>> beanClazzs = scannerPackages(basePackageName, resourceLoader, environment);
        beanClazzs.stream()
                .peek(this::preCheck)
                .map(clazz -> {
                    // 2.1 构造beanDefinition，设置生成代理类的FactoryBean以及其构造用的参数
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
                    GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

                    definition.getConstructorArgumentValues().addGenericArgumentValue(clazz);
                    definition.setBeanClass(getFactoryBeanClass());
                    return definition;
                })
                .forEach(beanDefinition -> {
                    // 2.2 向注册中心注册beanDefinition
                    beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                    registry.registerBeanDefinition(Objects.requireNonNull(beanDefinition.getBeanClassName()), beanDefinition);
                });
    }


    /**
     * 扫描指定包，过滤出需要代理的接口
     */
    default Set<Class<?>> scannerPackages(String basePackage, ResourceLoader resourceLoader, Environment environment) throws IOException {

        // 1. 构造基础包名，替换占位符
        ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(basePackage))
                + '/' + DEFAULT_RESOURCE_PATTERN;

        // 2. 获取基础包名下的所有资源
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

        // 3. 获取可读资源，生成对应的类，过滤出要代理的接口
        return Arrays.stream(resources)
                .filter(Resource::isReadable)
                .map(resource -> {
                    // 3.2 将资源转换为类
                    try {
                        return Class.forName(metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName());
                    } catch (ClassNotFoundException | IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(this::interfaceFilter)
                .collect(Collectors.toSet());
    }

}
