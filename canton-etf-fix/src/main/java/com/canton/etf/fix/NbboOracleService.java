package com.canton.etf.fix;

import com.canton.etf.common.ledger.LedgerCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NbboOracleService {

    private static final Logger log = LoggerFactory.getLogger(NbboOracleService.class);

    private final LedgerCommandService ledgerCommandService;
    private final WebClient webClient;
    private final String apiKey;

    public NbboOracleService(
            LedgerCommandService ledgerCommandService,
            WebClient.Builder webClientBuilder,
            @Value("${polygon.api.key}") String apiKey) {
        this.ledgerCommandService = ledgerCommandService;
        this.webClient = webClientBuilder
                .baseUrl("https://api.polygon.io")
                .build();
        this.apiKey = apiKey;
    }

    @Scheduled(fixedDelayString = "${canton.nbbo.interval:5000}")
    public void pollNbbo() {
        log.info("Polling NBBO from Polygon.io");
        // TODO: fetch NBBO quotes and post NBBOQuote contracts to ledger
    }

    public void postQuote(String symbol, double bid, double ask,
                          int bidSize, int askSize, String partyId) {
        log.info("Posting NBBO quote for {} bid={} ask={}", symbol, bid, ask);
        // TODO: create NBBOQuote contract on Canton ledger
    }
}