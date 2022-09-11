package com.szy.demo.controller;

import com.szy.demo.controller.vo.request.HttpTestRequest;
import com.szy.demo.controller.vo.response.CommonResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class HttpTestController {

    /*
    curl -X POST 'localhost:8080/test/echo/1?from=a&to=z' \
        -H 'Content-Type: application/json' \
        -H 'szy-header: yyds' \
        --data '{"username": "llj", "password": "lljpw", "message": "szy yyds"}'


     */
    @PostMapping("/echo/{userId}")
    public CommonResponse<String> echo(
            @RequestBody HttpTestRequest req,
            @RequestParam("from") String from,
            @RequestParam("to") String to,
            @PathVariable Integer userId,
            @RequestHeader("szy-header") String szyHeader) {
        String resp = String.format("Hello %s, userId = %d, password = %s, from = %s, to = %s, szy-header = %s, message = %s",
                req.getUsername(), userId, req.getPassword(), from, to, szyHeader, req.getMessage());
        return CommonResponse.success(resp);
    }

}
