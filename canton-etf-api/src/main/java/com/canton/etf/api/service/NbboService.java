package com.canton.etf.api.service;

import com.canton.etf.api.dto.NBBOQuoteResponse;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.pricing.nbboquote.NBBOQuote;
import com.daml.ledger.javaapi.data.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NbboService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public NbboService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    public List<NBBOQuoteResponse> getLatestQuotes(String partyId) {
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
                .filter(e -> e.getTemplateId().getEntityName().equals("NBBOQuote"))
                .map(NBBOQuote.Contract::fromCreatedEvent)
                .map(c -> new NBBOQuoteResponse(
                        c.data.symbol,
                        c.data.bidPrice,
                        c.data.askPrice,
                        c.data.timestamp
                ))
                .toList();
    }

    public List<NBBOQuoteResponse> getQuotesByTicker(String partyId, String ticker) {
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
                .filter(e -> e.getTemplateId().getEntityName().equals("NBBOQuote"))
                .map(NBBOQuote.Contract::fromCreatedEvent)
                .filter(c -> c.data.symbol.equals(ticker))
                .map(c -> new NBBOQuoteResponse(
                        c.data.symbol,
                        c.data.bidPrice,
                        c.data.askPrice,
                        c.data.timestamp
                ))
                .toList();
    }
}