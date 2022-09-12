package com.szy.demo.controller;

import com.szy.demo.controller.vo.response.CommonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/req")
public interface HttpController {

    @GetMapping("/test")
    public CommonResponse<String> postRequest();
}
