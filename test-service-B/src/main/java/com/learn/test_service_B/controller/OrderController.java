package com.learn.test_service_B.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OrderController {

    @GetMapping("/order")
    public String getOrder(){
        log.info("getOrder method invoked");
        return "<h2>This is the response from test-service-B</h2>";
    }
}
