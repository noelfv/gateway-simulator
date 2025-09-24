package com.bbva;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bbva")
public class SwingApplication {
    public static void main(String[] args) {
        SpringApplication.run(SwingApplication.class, args);
    }
}

