package com.szy.core.eunm;

import com.szy.core.exception.SzyException;

public enum SzyExceptionEnum {

    CONFIG_CENTER_METHOD_SIGNATURE_ERROR(100001, "方法签名错误,方法必须无参并且有返回值"),

    CONFIG_CENTER_ANNOTATION_NOT_FOUND(100002, "接口上使用@SConfigCenter, 返回值为基础类型的方法必须使用@SValue注解"),

    HTTP_CENTER_HTTPMETHOD_NOT_FOUND(200001, "请求方式必须设置"),

    HTTP_CENTER_HTTPSUBPATH_NOT_FOUND(200002, "请求子路径必须设置")
    ;


    private Integer code;
    private String msg;
    SzyExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SzyException toException() {
        return new SzyException(code, msg);
    }

    public void throwsIf(boolean condition) {
        if (condition) throw this.toException();
    }
}
