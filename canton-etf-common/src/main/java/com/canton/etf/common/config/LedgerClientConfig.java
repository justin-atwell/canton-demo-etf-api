package com.canton.etf.common.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LedgerClientConfig {

    private final String ledgerHost;
    private final int ledgerPort;

    public LedgerClientConfig(
            @Value("${canton.ledger.host}") String ledgerHost,
            @Value("${canton.ledger.port}") int ledgerPort) {
        this.ledgerHost = ledgerHost;
        this.ledgerPort = ledgerPort;
    }

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder
                .forAddress(ledgerHost, ledgerPort)
                .usePlaintext()
                .build();
    }
}