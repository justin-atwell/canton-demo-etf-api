package com.canton.etf.api.service;

import com.canton.etf.api.aspect.LogAccessEvent;
import com.canton.etf.api.dto.AddConstituentRequest;
import com.canton.etf.api.dto.ConstituentResponse;
import com.canton.etf.api.dto.CreateEtfRequest;
import com.canton.etf.api.dto.EtfResponse;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.fund.constituent.Constituent;
import com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition;
import com.daml.ledger.javaapi.data.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class EtfService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public EtfService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    @LogAccessEvent(action = "CREATE_ETF", resourceParam = "ticker")
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

    @LogAccessEvent(action = "VIEW_ETF", resourceParam = "ticker")
    public EtfResponse getEtf(String partyId, String ticker) {
        var events = ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId));
        log.info("Total events: {} partyId: {}", events.size(), partyId);

        ETFDefinition.Contract etf = events.stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("ETFDefinition"))
                .map(ETFDefinition.Contract::fromCreatedEvent)
                .filter(contract -> contract.data.ticker.equals(ticker))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ETF not found: " + ticker));

        List<ConstituentResponse> constituents = events.stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("Constituent"))
                .map(Constituent.Contract::fromCreatedEvent)
                .filter(c -> c.data.ticker.equals(ticker))
                .map(ConstituentResponse::from)
                .toList();

        return EtfResponse.from(etf, constituents);
    }

    @LogAccessEvent(action = "LIST_ALL_ETFS", resourceParam = "partyId")
    public List<String> listEtfs(String partyId) {
        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("ETFDefinition"))
                .map(ETFDefinition.Contract::fromCreatedEvent)
                .map(c -> c.data.ticker)
                .distinct()
                .sorted()
                .toList();
    }

    @LogAccessEvent(action = "SUSPEND_ETF", resourceParam = "ticker")
    public void suspendEtf(String partyId, String ticker) {
        ETFDefinition.Contract etf = findEtfContract(partyId, ticker)
                .orElseThrow(() -> new RuntimeException("ETF not found: " + ticker));

        var command = new ETFDefinition.ContractId(etf.id.contractId)
                .exerciseSuspend()
                .commands()
                .get(0);

        var submission = CommandsSubmission.create(
                        APP_ID,
                        UUID.randomUUID().toString(),
                        Optional.empty(),
                        List.of(command))
                .withActAs(partyId)
                .withActAs(etf.data.compliance);

        ledgerCommandService.submitAndWaitWithSubmission(submission);

        log.info("ETF suspended: ticker={} by party={}", ticker, partyId);
    }

    @LogAccessEvent(action = "ADD_CONSTITUENT", resourceParam = "ticker")
    public String addConstituent(String partyId, String ticker, AddConstituentRequest request) {
        ETFDefinition.Contract etf = findEtfContract(partyId, ticker)
                .orElseThrow(() -> new RuntimeException("ETF not found: " + ticker));

        var command = new ETFDefinition.ContractId(etf.id.contractId)
                .exerciseAddConstituent(
                        request.symbol(),
                        request.name(),
                        request.cusip(),
                        request.weight()
                )
                .commands()
                .get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));

        log.info("Constituent added: ticker={} symbol={} weight={}", ticker, request.symbol(), request.weight());
        return request.symbol();
    }

    @LogAccessEvent(action = "LIST_CONSTITUENTS", resourceParam = "ticker")
    public List<ConstituentResponse> getConstituents(String partyId, String ticker) {
        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("Constituent"))
                .map(Constituent.Contract::fromCreatedEvent)
                .filter(c -> c.data.ticker.equals(ticker))
                .map(ConstituentResponse::from)
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