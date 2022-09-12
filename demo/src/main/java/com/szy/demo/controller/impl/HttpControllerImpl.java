package com.szy.demo.controller.impl;

import com.szy.core.httpcenter.SzyHttpClientFactory;
import com.szy.core.util.GenericBuilder;
import com.szy.demo.controller.HttpController;
import com.szy.demo.controller.vo.request.HttpTestRequest;
import com.szy.demo.controller.vo.response.CommonResponse;
import com.szy.demo.httpcenter.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HttpControllerImpl implements HttpController {

    @Resource
    HttpRequest httpRequest;


    @Override
    public CommonResponse<String> postRequest() {

        HttpTestRequest body = GenericBuilder.of(HttpTestRequest::new)
                .with(HttpTestRequest::setMessage, "szy yyds")
                .with(HttpTestRequest::setUsername, "zx")
                .with(HttpTestRequest::setPassword, "zxpw")
                .build()
                ;

        return httpRequest.httpPostRequest(1, "a", "z", body);
    }
}
