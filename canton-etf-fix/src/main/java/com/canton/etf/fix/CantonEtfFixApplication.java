package com.canton.etf.fix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CantonEtfFixApplication {

    public static void main(String[] args) {
        SpringApplication.run(CantonEtfFixApplication.class, args);
    }
}