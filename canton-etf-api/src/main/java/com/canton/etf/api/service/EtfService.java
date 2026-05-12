package com.canton.etf.api.service;

import com.canton.etf.api.dto.CreateEtfRequest;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    public Object getEtf(String partyId, String ticker) {
        // TODO: query active ETFDefinition contracts from ledger
        return Map.of("ticker", ticker, "status", "Active", "message", "Ledger query coming soon");
    }

    public void suspendEtf(String partyId, String ticker) {
        // TODO: find ETFDefinition contract by ticker and exercise Suspend choice
        log.info("Suspend requested for ticker {} by party {}", ticker, partyId);
    }
}