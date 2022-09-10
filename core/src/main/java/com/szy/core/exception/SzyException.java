package com.szy.core.exception;

public class SzyException extends RuntimeException {

    private Integer code;
    public SzyException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

}
