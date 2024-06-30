package com.learn.test_service_B.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @GetMapping("/order")
    public String getOrder(){
        return "<h2>This is the response from test-service-B</h2>";
    }
}
