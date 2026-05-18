package com.canton.etf.api.service;

import com.canton.etf.api.dto.CreateNAVRequest;
import com.canton.etf.api.dto.NAVResponse;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition;
import com.canton.etf.model.canton.etf.pricing.nav.NAV;
import com.daml.ledger.javaapi.data.CumulativeFilter;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.Filter;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class NAVService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public NAVService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    // -------------------------------------------------------------------------
    // createNAV
    //   Immutable — no choices on NAV template.
    //   Signatory: fundManager (the calling party).
    //   Resolves custodian/compliance/auditor from ETFDefinition by ticker.
    //   Returns navDate as string — used as the created resource identifier.
    // -------------------------------------------------------------------------
    public String createNAV(String partyId, String ticker, CreateNAVRequest request) {
        ETFDefinition.Contract etf = findEtfContract(partyId, ticker)
                .orElseThrow(() -> new RuntimeException("ETF not found: " + ticker));

        var command = new NAV(
                partyId,                                    // fundManager
                etf.data.custodian,
                etf.data.compliance,
                etf.data.auditor,
                ticker,
                request.navDate(),
                BigDecimal.valueOf(request.navPerShare()),
                BigDecimal.valueOf(request.totalAUM()),
                request.source()
        ).create().commands().get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("NAV created: ticker={} date={} navPerShare={}", ticker, request.navDate(), request.navPerShare());

        return request.navDate().toString();
    }

    // -------------------------------------------------------------------------
    // getNAV
    //   Returns the most recent NAV contract for a ticker by navDate.
    // -------------------------------------------------------------------------
    public NAVResponse getNAV(String partyId, String ticker) {
        return getNavHistory(partyId, ticker)
                .stream()
                .max(Comparator.comparing(NAVResponse::navDate))
                .orElseThrow(() -> new RuntimeException("No NAV found for ticker: " + ticker));
    }

    // -------------------------------------------------------------------------
    // getNavHistory
    //   Returns all NAV contracts for a ticker sorted by navDate ascending.
    // -------------------------------------------------------------------------
    public List<NAVResponse> getNavHistory(String partyId, String ticker) {
        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("NAV"))
                .map(NAV.Contract::fromCreatedEvent)
                .filter(c -> c.data.ticker.equals(ticker))
                .map(NAVResponse::from)
                .sorted(Comparator.comparing(NAVResponse::navDate))
                .toList();
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private Optional<ETFDefinition.Contract> findEtfContract(String partyId, String ticker) {
        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("ETFDefinition"))
                .map(ETFDefinition.Contract::fromCreatedEvent)
                .filter(c -> c.data.ticker.equals(ticker))
                .findFirst();
    }

    private EventFormat buildEventFormat(String partyId) {
        return new EventFormat(
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
    }
}