package com.learn.test_service_C.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/product")
    public String getProduct(){
        return "<h2>This is the response from test-service-C</h2>";
    }
}
