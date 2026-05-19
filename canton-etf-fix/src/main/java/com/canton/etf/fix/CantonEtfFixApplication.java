package com.canton.etf.fix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {
        "com.canton.etf.fix",
        "com.canton.etf.common"
})
public class CantonEtfFixApplication {

    public static void main(String[] args) {
        SpringApplication.run(CantonEtfFixApplication.class, args);
    }
}