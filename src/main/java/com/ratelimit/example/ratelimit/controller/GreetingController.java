package com.ratelimit.example.ratelimit.controller;


import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

import com.ratelimit.example.ratelimit.service.Greeting;
import io.github.bucket4j.Bucket;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bucket4j;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final Bucket bucket;


    public GreetingController() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping("/greeting")
    public ResponseEntity greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok(new Greeting(counter.incrementAndGet(), String.format(template, name)));
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("TOO_MANY_REQUESTS");
        //return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
}