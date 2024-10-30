package com.learn.test_service_A.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequiredArgsConstructor
public class HystrixController {


    private final Tracer tracer;  // Injected tracer to get trace information

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
        log.info("carPurchaseOne method invoked");

        // Get the current span and trace context
        var currentSpan = tracer.currentSpan();

        if (currentSpan == null) {
            throw new IllegalStateException("No active span found, tracing is not working");
        }

        System.out.println("retry count:"+count);
        count++;
        HttpClient httpClient = HttpClient.newHttpClient();


        HttpRequest orderRequest = HttpRequest.newBuilder(
                        URI.create("http://localhost:8081/order"))
                .header("X-B3-TraceId", currentSpan.context().traceId())
                .header("X-B3-SpanId", currentSpan.context().spanId())
                .header("X-B3-Sampled", currentSpan.context().sampled() ? "1" : "0")
                .GET()
                .build();

        HttpResponse<String> orderResponse = httpClient.send(orderRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(orderResponse.body());

        HttpRequest productRequest = HttpRequest.newBuilder(
                        URI.create("http://localhost:8082/product"))
                .header("X-B3-TraceId", currentSpan.context().traceId())
                .header("X-B3-SpanId", currentSpan.context().spanId())
                .header("X-B3-Sampled", currentSpan.context().sampled() ? "1" : "0")
                .GET()
                .build();

        HttpResponse<String> productResponse = httpClient.send(productRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println(productResponse.body());

        return orderResponse.body() + " " + productResponse.body();
    }

    public String myFallBackMethod(Exception e){
        return "This is the fallback method";
    }
}
