package com.canton.etf.fix;

import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition;
import com.canton.etf.model.canton.etf.pricing.nbboquote.NBBOQuote;
import com.daml.ledger.javaapi.data.CumulativeFilter;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.Filter;
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

@Service
public class NbboOracleService {

    private static final Logger log = LoggerFactory.getLogger(NbboOracleService.class);
    private static final String APP_ID = "canton-etf-fix";

    private final LedgerCommandService ledgerCommandService;
    private final WebClient webClient;
    private final String apiKey;
    private final String marketMakerPartyId;

    public NbboOracleService(
            LedgerCommandService ledgerCommandService,
            WebClient.Builder webClientBuilder,
            @Value("${polygon.api.key}") String apiKey,
            @Value("${canton.market-maker.party-id}") String marketMakerPartyId) {
        this.ledgerCommandService = ledgerCommandService;
        this.webClient = webClientBuilder
                .baseUrl("https://api.polygon.io")
                .build();
        this.apiKey = apiKey;
        this.marketMakerPartyId = marketMakerPartyId;
    }

    // -------------------------------------------------------------------------
    // pollNbbo
    //   Scheduled — fires every canton.nbbo.interval ms (default 5000).
    //   Fetches NBBO quotes from Polygon.io for all active ETF tickers,
    //   posts NBBOQuote contracts to the Canton ledger.
    // -------------------------------------------------------------------------
    @Scheduled(fixedDelayString = "${canton.nbbo.interval:5000}")
    public void pollNbbo() {
        log.info("Polling NBBO from Polygon.io");

        // Resolve active ETF tickers from the ledger
        List<String> tickers = getActiveTickers();
        if (tickers.isEmpty()) {
            log.debug("No active ETFs found — skipping NBBO poll");
            return;
        }

        for (String ticker : tickers) {
            try {
                fetchAndPostQuote(ticker);
            } catch (Exception e) {
                log.error("Failed to post NBBO quote for ticker={}: {}", ticker, e.getMessage());
            }
        }
    }

    // -------------------------------------------------------------------------
    // postQuote
    //   Creates an NBBOQuote contract on the Canton ledger.
    //   Signatory: marketMaker
    //   Resolves fundManager/compliance/auditor from ETFDefinition by symbol.
    // -------------------------------------------------------------------------
    public void postQuote(String symbol, double bid, double ask,
                          int bidSize, int askSize, String partyId) {
        log.info("Posting NBBO quote for {} bid={} ask={}", symbol, bid, ask);

        ETFDefinition.Contract etf = findEtfContract(partyId, symbol)
                .orElseThrow(() -> new RuntimeException("ETF not found for symbol: " + symbol));

        var command = new NBBOQuote(
                partyId,                            // marketMaker
                etf.data.fundManager,
                etf.data.compliance,
                etf.data.auditor,
                symbol,
                BigDecimal.valueOf(bid),
                BigDecimal.valueOf(ask),
                (long) bidSize,
                (long) askSize,
                Instant.now()                       // timestamp
        ).create().commands().get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("NBBOQuote posted: symbol={} bid={} ask={}", symbol, bid, ask);
    }

    // -------------------------------------------------------------------------
    // Internal — fetch quote from Polygon.io and post to ledger
    // -------------------------------------------------------------------------
    private void fetchAndPostQuote(String ticker) {
        // Polygon.io snapshot endpoint: /v2/snapshot/locale/us/markets/stocks/tickers/{ticker}
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/snapshot/locale/us/markets/stocks/tickers/{ticker}")
                        .queryParam("apiKey", apiKey)
                        .build(ticker))
                .retrieve()
                .bodyToMono(PolygonSnapshotResponse.class)
                .subscribe(response -> {
                    if (response != null && response.ticker() != null) {
                        var nbbo = response.ticker().day();
                        if (nbbo != null) {
                            postQuote(
                                    ticker,
                                    nbbo.o(),   // open as bid proxy — replace with lastQuote when available
                                    nbbo.c(),   // close as ask proxy
                                    100,        // default size — Polygon free tier doesn't include size
                                    100,
                                    marketMakerPartyId
                            );
                        }
                    }
                }, error -> log.error("Polygon.io fetch failed for {}: {}", ticker, error.getMessage()));
    }

    // -------------------------------------------------------------------------
    // Internal — resolve active ETF tickers from ACS
    // -------------------------------------------------------------------------
    private List<String> getActiveTickers() {
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

        return ledgerCommandService.getActiveContracts(marketMakerPartyId, eventFormat)
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("ETFDefinition"))
                .map(ETFDefinition.Contract::fromCreatedEvent)
                .filter(c -> "Active".equals(c.data.status))
                .map(c -> c.data.ticker)
                .toList();
    }

    private Optional<ETFDefinition.Contract> findEtfContract(String partyId, String ticker) {
        EventFormat eventFormat = new EventFormat(
                Map.of(
                        partyId,
                        new CumulativeFilter(
                                Map.of(),
                                Map.of(),
                                Optional.of(Filter.Wildcard.HIDE_CREATED_EVENT_BLOB)
                        )
                ),
                Optional.empty(),
                true
        );

        return ledgerCommandService.getActiveContracts(partyId, eventFormat)
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("ETFDefinition"))
                .map(ETFDefinition.Contract::fromCreatedEvent)
                .filter(c -> c.data.ticker.equals(ticker))
                .findFirst();
    }

    // -------------------------------------------------------------------------
    // Polygon.io response shape (free tier snapshot)
    // -------------------------------------------------------------------------
    record PolygonSnapshotResponse(TickerSnapshot ticker) {}
    record TickerSnapshot(DayData day) {}
    record DayData(double o, double h, double l, double c, double v) {}
}