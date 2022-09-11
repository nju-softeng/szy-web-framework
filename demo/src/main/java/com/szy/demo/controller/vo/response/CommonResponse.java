package com.szy.demo.controller.vo.response;

import lombok.Data;

@Data
public class CommonResponse<T> {

    private Integer code;
    private String msg;
    private T data;

    public CommonResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(0, "success", data);
    }

}
