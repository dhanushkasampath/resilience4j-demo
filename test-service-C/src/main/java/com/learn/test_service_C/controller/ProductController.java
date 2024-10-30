package com.learn.test_service_C.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ProductController {

    @GetMapping("/product")
    public String getProduct(){
        log.info("getProduct method invoked");
        return "<h2>This is the response from test-service-C</h2>";
    }
}
