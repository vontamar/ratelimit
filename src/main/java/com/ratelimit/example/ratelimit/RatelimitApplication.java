package com.ratelimit.example.ratelimit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RatelimitApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatelimitApplication.class, args);
	}

}
