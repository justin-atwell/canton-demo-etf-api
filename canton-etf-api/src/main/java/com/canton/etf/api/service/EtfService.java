package com.canton.etf.api.service;

import com.canton.etf.api.dto.CreateEtfRequest;
import com.canton.etf.api.dto.EtfResponse;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition;
import com.daml.ledger.javaapi.data.CumulativeFilter;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.Filter;
import com.daml.ledger.javaapi.data.Identifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class EtfService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public EtfService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    public String createEtf(String partyId, CreateEtfRequest request) {
        var template = new ETFDefinition(
                partyId,
                request.custodian(),
                request.compliance(),
                request.auditor(),
                request.ticker(),
                request.name(),
                request.cusip(),
                "Active",
                LocalDate.now()
        );

        var command = template.create().commands().get(0);

        ledgerCommandService.submitAndWait(
                partyId,
                APP_ID,
                List.of(command)
        );

        return request.ticker();
    }
    public EtfResponse getEtf(String partyId, String ticker) {
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

        var events = ledgerCommandService.getActiveContracts(partyId, eventFormat);
        log.info("Total events: {} partyId: {}", events.size(), partyId);

// Temporary: also try querying with anyPartyFilter instead
        EventFormat anyPartyFormat = new EventFormat(
                Map.of(),
                Optional.of(new CumulativeFilter(
                        Map.of(),
                        Map.of(),
                        Optional.of(Filter.Wildcard.HIDE_CREATED_EVENT_BLOB)
                )),
                true
        );
        var anyEvents = ledgerCommandService.getActiveContracts(partyId, anyPartyFormat);
        log.info("anyPartyFilter events: {}", anyEvents.size());

        return events.stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("ETFDefinition"))
                .map(ETFDefinition.Contract::fromCreatedEvent)
                .filter(contract -> contract.data.ticker.equals(ticker))
                .findFirst()
                .map(EtfResponse::from)
                .orElseThrow(() -> new RuntimeException("ETF not found: " + ticker));
    }

    public void suspendEtf(String partyId, String ticker) {
        // TODO: find ETFDefinition contract by ticker and exercise Suspend choice
        log.info("Suspend requested for ticker {} by party {}", ticker, partyId);
    }
}