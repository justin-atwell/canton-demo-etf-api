package com.canton.etf.api.service;

import com.canton.etf.api.dto.MarginCallV2Dto;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.DeclareDefault;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallStatus;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallV2;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.RespondToCall;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.ResponseType;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.SatisfyCall;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.UpdateCoverage;
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
import java.util.UUID;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class MarginCallV2Service {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public MarginCallV2Service(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    public MarginCallV2Dto issueMarginCall(String partyId, MarginCallV2Dto request) {
        String callId = request.callId() != null ? request.callId() : UUID.randomUUID().toString();
        Instant now = Instant.now();

        var command = new MarginCallV2(
                request.primeBroker(),
                request.hedgeFund(),
                request.riskManager(),
                callId,
                new CollateralPool.ContractId(request.poolCid()),
                BigDecimal.valueOf(request.requiredCoverage()),
                BigDecimal.valueOf(request.currentCoverage()),
                BigDecimal.valueOf(request.shortfall()),
                request.responseDeadline() != null ? Instant.parse(request.responseDeadline()) : now,
                MarginCallStatus.ISSUED,
                now,
                now
        ).create().commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("MarginCallV2 issued: callId={} primeBroker={}", callId, request.primeBroker());

        return getCallByCallId(partyId, callId);
    }

    public List<MarginCallV2Dto> getActiveMarginCalls(String partyId) {
        return getActiveCalls(partyId).stream()
                .map(MarginCallV2.Contract::fromCreatedEvent)
                .filter(c -> c.data.status == MarginCallStatus.ISSUED
                        || c.data.status == MarginCallStatus.RESPONSERECEIVED)
                .map(this::toDto)
                .toList();
    }

    public MarginCallV2Dto respondToCall(String partyId, String contractId, ResponseType responseType, String comment) {
        var command = new MarginCallV2.ContractId(contractId)
                .exerciseRespondToCall(new RespondToCall(responseType, comment))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("MarginCallV2.RespondToCall exercised: contractId={}", contractId);

        return getCall(partyId, contractId);
    }

    public MarginCallV2Dto satisfyCall(String partyId, String contractId, double newCoverage) {
        var command = new MarginCallV2.ContractId(contractId)
                .exerciseSatisfyCall(new SatisfyCall(BigDecimal.valueOf(newCoverage)))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("MarginCallV2.SatisfyCall exercised: contractId={}", contractId);

        return getCall(partyId, contractId);
    }

    public MarginCallV2Dto declareDefault(String partyId, String contractId) {
        var command = new MarginCallV2.ContractId(contractId)
                .exerciseDeclareDefault(new DeclareDefault())
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("MarginCallV2.DeclareDefault exercised: contractId={}", contractId);

        return getCall(partyId, contractId);
    }

    public MarginCallV2Dto updateCoverage(String partyId, String contractId, double newCoverage) {
        var command = new MarginCallV2.ContractId(contractId)
                .exerciseUpdateCoverage(new UpdateCoverage(BigDecimal.valueOf(newCoverage)))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("MarginCallV2.UpdateCoverage exercised: contractId={} newCoverage={}", contractId, newCoverage);

        return getCall(partyId, contractId);
    }

    // --- Internal helpers ---

    private MarginCallV2Dto getCall(String partyId, String contractId) {
        CreatedEvent event = findEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("MarginCallV2 not found: " + contractId));
        return toDto(MarginCallV2.Contract.fromCreatedEvent(event));
    }

    private MarginCallV2Dto getCallByCallId(String partyId, String callId) {
        return getActiveCalls(partyId).stream()
                .map(MarginCallV2.Contract::fromCreatedEvent)
                .filter(c -> c.data.callId.equals(callId))
                .findFirst()
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("MarginCallV2 not found after create: " + callId));
    }

    private Optional<CreatedEvent> findEventByContractId(String partyId, String contractId) {
        return getActiveCalls(partyId).stream()
                .filter(e -> e.getContractId().equals(contractId))
                .findFirst();
    }

    private List<CreatedEvent> getActiveCalls(String partyId) {
        return ledgerCommandService.getActiveContracts(
                partyId,
                buildEventFormat(partyId)
        );
    }

    private EventFormat buildEventFormat(String partyId) {
        return new EventFormat(
                Map.of(partyId, new CumulativeFilter(
                        Map.of(), Map.of(),
                        Optional.of(Filter.Wildcard.HIDE_CREATED_EVENT_BLOB)
                )),
                Optional.empty(),
                true
        );
    }

    // --- Mapping ---

    private MarginCallV2Dto toDto(MarginCallV2.Contract contract) {
        return new MarginCallV2Dto(
                contract.id.contractId,
                contract.data.primeBroker,
                contract.data.hedgeFund,
                contract.data.riskManager,
                contract.data.callId,
                contract.data.poolCid.toString(),
                contract.data.requiredCoverage.doubleValue(),
                contract.data.currentCoverage.doubleValue(),
                contract.data.shortfall.doubleValue(),
                contract.data.responseDeadline != null ? contract.data.responseDeadline.toString() : null,
                mapStatus(contract.data.status),
                contract.data.issuedAt != null ? contract.data.issuedAt.toString() : null,
                contract.data.updatedAt != null ? contract.data.updatedAt.toString() : null
        );
    }

    private MarginCallV2Dto.Status mapStatus(MarginCallStatus status) {
        if (status == MarginCallStatus.ISSUED) return MarginCallV2Dto.Status.ISSUED;
        if (status == MarginCallStatus.RESPONSERECEIVED) return MarginCallV2Dto.Status.RESPONSERECEIVED;
        if (status == MarginCallStatus.SATISFIED) return MarginCallV2Dto.Status.SATISFIED;
        if (status == MarginCallStatus.DEFAULTED) return MarginCallV2Dto.Status.DEFAULTED;
        throw new IllegalArgumentException("Unknown MarginCallStatus: " + status);
    }
}