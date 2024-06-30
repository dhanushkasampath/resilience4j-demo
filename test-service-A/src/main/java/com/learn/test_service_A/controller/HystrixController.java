package com.learn.test_service_A.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * How circuit breaker / retry & rate limiter works
 * http://localhost:8080/actuator/health
 *
 *
 * if you have implemented the circuit breaker pattern, we can extend it to retry mechanism also
 */
@RestController
public class HystrixController {

    private static int count = 0;
    private static final String SERVICE_A = "serviceA";

    @GetMapping("/home")
    public String base(){
        return "<h1>This is the home page for Hystrix Service</h1>";
    }

    @CircuitBreaker(name = SERVICE_A, fallbackMethod = "myFallBackMethod")
//    @Retry(name = SERVICE_A, fallbackMethod = "myFallBackMethod")
//    @RateLimiter(name = SERVICE_A)
    @GetMapping("/firstDemo")
    public String carPurchaseOne() throws IOException, InterruptedException {
        System.out.println("retry count:"+count);
        count++;
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest orderRequest = HttpRequest.newBuilder(
                        URI.create("http://localhost:8081/order")).GET()
                .build();

        HttpResponse<String> orderResponse = httpClient.send(orderRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(orderResponse.body());

        HttpRequest productRequest = HttpRequest.newBuilder(
                        URI.create("http://localhost:8082/product")).GET()
                .build();

        HttpResponse<String> productResponse = httpClient.send(productRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(productResponse.body());

        return orderResponse.body() + " " + productResponse.body();
    }

    public String myFallBackMethod(Exception e){
        return "This is the fallback method";
    }
}
