package com.canton.etf.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.canton.etf.api",
        "com.canton.etf.common"
})
public class CantonEtfApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CantonEtfApiApplication.class, args);
    }
}