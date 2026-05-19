package com.canton.etf.fix;

import com.canton.etf.common.ledger.LedgerCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NbboOracleServiceTest {

    @Mock
    private LedgerCommandService ledgerCommandService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    private NbboOracleService nbboOracleService;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(ledgerCommandService.getActiveContracts(any(), any()))
                .thenReturn(List.of());
        nbboOracleService = new NbboOracleService(
                ledgerCommandService,
                webClientBuilder
        );
    }

}