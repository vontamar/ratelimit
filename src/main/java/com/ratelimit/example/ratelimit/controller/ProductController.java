package com.ratelimit.example.ratelimit.controller;


import com.ratelimit.example.ratelimit.data.entity.Product;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {


    public Bucket createNewBucket() {
        long capacity = 10;
        Refill refill = Refill.greedy(10, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(HttpServletRequest httpRequest ) {
        HttpSession session = httpRequest.getSession(true);
        String appKey =String.valueOf("<Security Key> ");  //SecurityUtils.getThirdPartyAppKey();
        Bucket bucket = (Bucket) session.getAttribute("throttler-" + appKey);
        if (bucket == null) {
            bucket = createNewBucket();
            session.setAttribute("throttler-" + appKey, bucket);
        }
        boolean okToGo = bucket.tryConsume(10);
        if (okToGo) {
            return new ResponseEntity<List<Product>>(
                    Arrays.asList(new Product("product 1", "category 1", new Date()),
                            new Product("product 2", "category 1", new Date()),
                            new Product("product 3", "category 2", new Date())
                    ),
                    HttpStatus.OK);
        }
        else
            return new ResponseEntity("You have exceeded the 10 requests in 1 minute limit!", HttpStatus.TOO_MANY_REQUESTS);
    }
}