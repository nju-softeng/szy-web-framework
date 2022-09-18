package com.szy.demo.httpcenter;

import com.szy.core.httpcenter.SzyHttpMethod;
import com.szy.core.httpcenter.annotation.*;
import com.szy.demo.controller.vo.request.HttpTestRequest;
import com.szy.demo.controller.vo.response.CommonResponse;

@SHttpCenter
@SHttpPath("http://localhost:8080")
public interface HttpRequest {
    @SHttpSubPath("/test/echo/{0}")
    @SHttpMethod(SzyHttpMethod.POST)
    @SHttpHeader(key = "content-type", value = "application/json")
    @SHttpHeader(key = "szy-header", value = "yyds")
    CommonResponse<String> httpPostRequest(
            @SPathVar("userId") int userId,
            @SRequestParam("from") String from,
            @SRequestParam("to") String to,
            @SHttpBody HttpTestRequest body
    );
}
