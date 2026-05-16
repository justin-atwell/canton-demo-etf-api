package com.canton.etf.api.service;

import com.canton.etf.api.dto.CreateRebalanceRequest;
import com.canton.etf.api.dto.RebalanceProposalResponse;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition;
import com.canton.etf.model.canton.etf.rebalance.rebalanceproposal.RebalanceProposal;
import com.canton.etf.model.da.types.Tuple2;
import com.daml.ledger.javaapi.data.Command;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.CumulativeFilter;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.Filter;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class RebalanceService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public RebalanceService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    // -------------------------------------------------------------------------
    // propose
    //   Resolves custodian/compliance/auditor from ETFDefinition by ticker.
    //   Signatory: fundManager (the calling party).
    //   Returns proposalId — controller sends it back as 201 body.
    // -------------------------------------------------------------------------
    public String propose(String partyId, String ticker, CreateRebalanceRequest request) {
        ETFDefinition.Contract etf = findEtfContract(partyId, ticker)
                .orElseThrow(() -> new RuntimeException("ETF not found: " + ticker));

        var command = new RebalanceProposal(
                partyId,                           // fundManager
                etf.data.custodian,
                etf.data.compliance,
                etf.data.auditor,
                ticker,
                request.proposalId(),
                toWeightList(request.newWeights()),
                Instant.now(),                     // proposedAt
                "Pending"
        ).create().commands().get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));

        log.info("RebalanceProposal created: proposalId={} ticker={}", request.proposalId(), ticker);
        return request.proposalId();
    }

    // -------------------------------------------------------------------------
    // getProposal
    // -------------------------------------------------------------------------
    public RebalanceProposalResponse getProposal(String partyId, String ticker, String proposalId) {
        return findProposalContract(partyId, ticker, proposalId)
                .map(RebalanceProposalResponse::from)
                .orElseThrow(() -> new RuntimeException(
                        "RebalanceProposal not found: " + proposalId + " ticker: " + ticker));
    }

    // -------------------------------------------------------------------------
    // approve
    //   Controller: compliance
    // -------------------------------------------------------------------------
    public void approve(String partyId, String ticker, String proposalId) {
        CreatedEvent event = findProposalEvent(partyId, ticker, proposalId)
                .orElseThrow(() -> new RuntimeException(
                        "RebalanceProposal not found for approve: " + proposalId));

        Command command = new RebalanceProposal.ContractId(event.getContractId())
                .exerciseApprove()
                .commands()
                .get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("RebalanceProposal approved: proposalId={}", proposalId);
    }

    // -------------------------------------------------------------------------
    // reject
    //   Controller: compliance
    //   Archives original, creates new contract with status "Rejected"
    // -------------------------------------------------------------------------
    public void reject(String partyId, String ticker, String proposalId) {
        CreatedEvent event = findProposalEvent(partyId, ticker, proposalId)
                .orElseThrow(() -> new RuntimeException(
                        "RebalanceProposal not found for reject: " + proposalId));

        Command command = new RebalanceProposal.ContractId(event.getContractId())
                .exerciseReject()
                .commands()
                .get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("RebalanceProposal rejected: proposalId={}", proposalId);
    }

    // -------------------------------------------------------------------------
    // execute
    //   Controller: fundManager
    //   Status guard mirrors the assert in Daml — belt and suspenders.
    // -------------------------------------------------------------------------
    public void execute(String partyId, String ticker, String proposalId) {
        CreatedEvent event = findProposalEvent(partyId, ticker, proposalId)
                .orElseThrow(() -> new RuntimeException(
                        "RebalanceProposal not found for execute: " + proposalId));

        RebalanceProposal.Contract contract = RebalanceProposal.Contract.fromCreatedEvent(event);
        if (!"Approved".equals(contract.data.status)) {
            throw new IllegalStateException(
                    "Cannot execute proposal " + proposalId
                            + " — status is '" + contract.data.status + "', expected 'Approved'");
        }

        Command command = new RebalanceProposal.ContractId(event.getContractId())
                .exerciseExecute()
                .commands()
                .get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("RebalanceProposal executed: proposalId={}", proposalId);
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

    private Optional<RebalanceProposal.Contract> findProposalContract(
            String partyId, String ticker, String proposalId) {
        return findProposalEvent(partyId, ticker, proposalId)
                .map(RebalanceProposal.Contract::fromCreatedEvent);
    }

    private Optional<CreatedEvent> findProposalEvent(
            String partyId, String ticker, String proposalId) {
        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("RebalanceProposal"))
                .filter(e -> {
                    RebalanceProposal.Contract c = RebalanceProposal.Contract.fromCreatedEvent(e);
                    return c.data.ticker.equals(ticker) && c.data.proposalId.equals(proposalId);
                })
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

    private List<Tuple2<String, BigDecimal>> toWeightList(
            List<CreateRebalanceRequest.WeightEntry> entries) {
        return entries.stream()
                .map(e -> new Tuple2<>(e.symbol(), BigDecimal.valueOf(e.weight())))
                .toList();
    }
}