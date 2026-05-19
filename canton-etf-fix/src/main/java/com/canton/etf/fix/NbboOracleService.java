package com.canton.etf.fix;

import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition;
import com.canton.etf.model.canton.etf.pricing.nbboquote.NBBOQuote;
import com.daml.ledger.javaapi.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class NbboOracleService {

    private static final Logger log = LoggerFactory.getLogger(NbboOracleService.class);
    private static final String APP_ID = "canton-etf-fix";

    private final LedgerCommandService ledgerCommandService;
    private final WebClient polygonClient;

    @Value("${canton.market-maker.party-id}")
    private String marketMakerPartyId;

    @Value("${polygon.api.key}")
    private String polygonApiKey;

    @Value("${canton.fund-manager.party-id}")
    private String fundManagerPartyId;

    public NbboOracleService(LedgerCommandService ledgerCommandService,
                             WebClient.Builder webClientBuilder) {
        this.ledgerCommandService = ledgerCommandService;
        this.polygonClient = webClientBuilder
                .baseUrl("https://api.polygon.io")
                .build();
    }

    // -------------------------------------------------------------------------
    // Scheduled poller
    // -------------------------------------------------------------------------

    @Scheduled(fixedDelayString = "${canton.nbbo.interval:30000}")
    public void pollNbbo() {
        log.info("NBBO poll starting");

        List<ETFDefinition.Contract> etfs = getActiveEtfs();

        if (etfs.isEmpty()) {
            log.info("No active ETFDefinition contracts found — skipping poll");
            return;
        }

        for (ETFDefinition.Contract etf : etfs) {
            try {
                fetchAndPost(etf);
            } catch (Exception e) {
                log.error("Failed to post NBBO quote for ticker={}: {}", etf.data.ticker, e.getMessage());
            }
        }
    }

    // -------------------------------------------------------------------------
    // Fetch from Polygon, consume stale quotes, post fresh quote
    // -------------------------------------------------------------------------

    private void fetchAndPost(ETFDefinition.Contract etf) {
        String ticker = etf.data.ticker;

        PolygonAggsResponse response = polygonClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/aggs/ticker/{ticker}/prev")
                        .queryParam("apiKey", polygonApiKey)
                        .build(ticker))
                .retrieve()
                .bodyToMono(PolygonAggsResponse.class)
                .block();

        if (response == null || response.results() == null || response.results().isEmpty()) {
            log.warn("No Polygon data for ticker={}", ticker);
            return;
        }

        consumeStaleQuotes(ticker);

        double bid = response.results().get(0).o();
        double ask = response.results().get(0).c();

        postQuote(etf, bid, ask);
    }

    public void postQuote(ETFDefinition.Contract etf, double bid, double ask) {
        var quote = new NBBOQuote(
                marketMakerPartyId,
                etf.data.fundManager,
                etf.data.compliance,
                etf.data.auditor,
                etf.data.ticker,
                BigDecimal.valueOf(bid),
                BigDecimal.valueOf(ask),
                100L,
                100L,
                Instant.now()
        );

        var command = quote.create().commands().get(0);

        ledgerCommandService.submitAndWait(
                marketMakerPartyId,
                APP_ID,
                List.of(command)
        );

        log.info("NBBOQuote posted: ticker={} bid={} ask={}", etf.data.ticker, bid, ask);
    }

    // -------------------------------------------------------------------------
    // Consume all stale NBBOQuote contracts for a ticker
    // -------------------------------------------------------------------------

    private void consumeStaleQuotes(String ticker) {
        EventFormat eventFormat = new EventFormat(
                Map.of(
                        marketMakerPartyId,
                        new CumulativeFilter(
                                Map.of(),
                                Map.of(),
                                Optional.of(Filter.Wildcard.HIDE_CREATED_EVENT_BLOB)
                        )
                ),
                Optional.empty(),
                true
        );

        List<NBBOQuote.Contract> stale = ledgerCommandService
                .getActiveContracts(marketMakerPartyId, eventFormat)
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("NBBOQuote"))
                .map(NBBOQuote.Contract::fromCreatedEvent)
                .filter(c -> c.data.symbol.equals(ticker))
                .toList();

        if (stale.isEmpty()) return;

        log.info("Consuming {} stale NBBOQuote(s) for ticker={}", stale.size(), ticker);

        List<Command> consumeCommands = stale.stream()
                .map(c -> new NBBOQuote.ContractId(c.id.contractId)
                        .exerciseConsume()
                        .commands()
                        .get(0))
                .toList();

        var submission = CommandsSubmission.create(
                        APP_ID,
                        UUID.randomUUID().toString(),
                        Optional.empty(),
                        consumeCommands)
                .withActAs(marketMakerPartyId);

        ledgerCommandService.submitAndWaitWithSubmission(submission);
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private List<ETFDefinition.Contract> getActiveEtfs() {
        EventFormat eventFormat = new EventFormat(
                Map.of(
                        fundManagerPartyId,
                        new CumulativeFilter(
                                Map.of(),
                                Map.of(),
                                Optional.of(Filter.Wildcard.HIDE_CREATED_EVENT_BLOB)
                        )
                ),
                Optional.empty(),
                true
        );

        return ledgerCommandService.getActiveContracts(fundManagerPartyId, eventFormat)
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("ETFDefinition"))
                .map(ETFDefinition.Contract::fromCreatedEvent)
                .toList();
    }

    // -------------------------------------------------------------------------
    // Polygon response records
    // -------------------------------------------------------------------------

    record PolygonAggsResponse(String ticker, List<AggResult> results) {}
    record AggResult(double o, double h, double l, double c, double v) {}
}