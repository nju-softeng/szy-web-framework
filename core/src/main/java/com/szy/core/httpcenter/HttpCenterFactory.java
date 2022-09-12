package com.szy.core.httpcenter;

import com.szy.core.eunm.SzyExceptionEnum;
import com.szy.core.httpcenter.annotation.*;
import com.szy.core.util.GenericBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.CallbackHelper;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.NoOp;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class HttpCenterFactory<T> implements FactoryBean<T> {

    private Class<T> interfaceType;

    @Resource
    SzyHttpClientFactory szyHttpClientFactory;

    SzyHttpClient szyHttpClient;
    private final InvocationHandler invocationHandler = (o, method1, objects) -> {

        SzyExceptionEnum.HTTP_CENTER_HTTPSUBPATH_NOT_FOUND
                .throwsIf(!method1.isAnnotationPresent(SHttpSubPath.class));

        SzyExceptionEnum.HTTP_CENTER_HTTPMETHOD_NOT_FOUND
                .throwsIf(!method1.isAnnotationPresent(SHttpMethod.class));

        szyHttpClient = szyHttpClientFactory.getClient();

        String url = method1.getAnnotation(SHttpSubPath.class).value();

        Annotation[][] parameterAnnotations = method1.getParameterAnnotations();

        String[] pathVars = new String[parameterAnnotations.length];
        int pathVarIndex = 0;
        for(int i = 0; i < parameterAnnotations.length; i ++){
            if(parameterAnnotations[i][0].annotationType() == SPathVar.class){
                pathVars[pathVarIndex ++] = objects[i].toString();
            }

        }
        if(pathVarIndex > 0){
            url = MessageFormat.format(url, pathVars);
        }

        String httpMethod = method1.getAnnotation(SHttpMethod.class).value();
        szyHttpClient.setHttpMethod(httpMethod);

        if(interfaceType.isAnnotationPresent(SHttpPath.class)){
            url = interfaceType.getAnnotation(SHttpPath.class).value() + url;
        }


        if (method1.isAnnotationPresent(SHttpHeaders.class)) {
            SHttpHeader[] annotationsByType = method1.getAnnotationsByType(SHttpHeader.class);
            log.info("annotationsByType:" + annotationsByType);
            for (SHttpHeader annotation : annotationsByType) {
                szyHttpClient.setHeader(annotation.key(), annotation.value());
            }
        }

        HashMap<String, Object> requestParammap = new HashMap<>();
        for(int i = 0; i < parameterAnnotations.length; i ++){
            if(parameterAnnotations[i][0] instanceof SRequestParam){
                requestParammap.put(((SRequestParam) parameterAnnotations[i][0]).value(), objects[i]);
            }
        }
        if(requestParammap.size() > 0){
            url = url + "?";
            for(Map.Entry<String, Object> entry : requestParammap.entrySet()){
                url = url + entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        szyHttpClient.setUrl(url);


        int bodyIndex = -1;
        for(int i = 0; i < parameterAnnotations.length; i ++){
            if(parameterAnnotations[i][0].annotationType() == SHttpBody.class){
                bodyIndex = i;
            }
        }
        if(bodyIndex != -1){
            szyHttpClient.setBody(objects[bodyIndex]);
        }

        Type type = method1.getGenericReturnType();

        return szyHttpClient.excute(type);

    };

    /**
     * 由 beanDefinition 注入
     */
    public HttpCenterFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() {
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

    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
