package com.canton.etf.api.service;

import com.canton.etf.api.dto.LiquidationAuditEventDto;
import com.canton.etf.api.dto.LiquidationWaterfallDto;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool;
import com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.ExecuteWaterfall;
import com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationAuditEvent;
import com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationPriority;
import com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationWaterfall;
import com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.WaterfallStatus;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallV2;
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
import java.util.stream.Collectors;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class LiquidationWaterfallService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public LiquidationWaterfallService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    public LiquidationWaterfallDto initiateWaterfall(String partyId, LiquidationWaterfallDto request) {
        String waterfallId = request.waterfallId() != null ? request.waterfallId() : UUID.randomUUID().toString();
        Instant now = Instant.now();

        var command = new LiquidationWaterfall(
                request.primeBroker(),
                request.hedgeFund(),
                request.riskManager(),
                waterfallId,
                new MarginCallV2.ContractId(request.marginCallCid()),
                new CollateralPool.ContractId(request.poolCid()),
                BigDecimal.valueOf(request.originalShortfall()),
                BigDecimal.valueOf(request.originalShortfall()),  // remainingShortfall = originalShortfall on creation
                List.of(),                                         // liquidationSteps start empty
                WaterfallStatus.INPROGRESS,
                now,
                now
        ).create().commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("LiquidationWaterfall initiated: waterfallId={} marginCallCid={}", waterfallId, request.marginCallCid());

        return getWaterfallByWaterfallId(partyId, waterfallId);
    }

    /**
     * ExecuteWaterfall takes List<LiquidationPriority> — priorities define the liquidation order.
     * MMF first, then Treasury, then BTC per the prime brokerage spec.
     * Caller can pass custom priorities via request body; defaults to standard order if empty.
     */
    public LiquidationWaterfallDto executeWaterfall(String partyId, String contractId,
                                                    List<LiquidationPriority> priorities) {
        var command = new LiquidationWaterfall.ContractId(contractId)
                .exerciseExecuteWaterfall(new ExecuteWaterfall(priorities))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("LiquidationWaterfall.ExecuteWaterfall exercised: contractId={}", contractId);

        return getWaterfall(partyId, contractId);
    }

    public LiquidationWaterfallDto getWaterfall(String partyId, String contractId) {
        CreatedEvent event = findWaterfallEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("LiquidationWaterfall not found: " + contractId));
        return toDto(LiquidationWaterfall.Contract.fromCreatedEvent(event));
    }

    public List<LiquidationAuditEventDto> getAuditEvents(String partyId, String waterfallId) {
        return ledgerCommandService.getActiveContracts(
                        partyId,
                        buildEventFormat(partyId)
                ).stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("LiquidationAuditEvent"))
                .map(LiquidationAuditEvent.Contract::fromCreatedEvent)
                .filter(c -> waterfallId.equals(c.data.waterfallId))
                .map(this::auditEventToDto)
                .toList();
    }

    public List<LiquidationWaterfallDto> getWaterfalls(String partyId) {
        return getActiveWaterfalls(partyId).stream()
                .map(LiquidationWaterfall.Contract::fromCreatedEvent)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // --- Internal helpers ---

    private LiquidationWaterfallDto getWaterfallByWaterfallId(String partyId, String waterfallId) {
        return getActiveWaterfalls(partyId).stream()
                .map(LiquidationWaterfall.Contract::fromCreatedEvent)
                .filter(c -> c.data.waterfallId.equals(waterfallId))
                .findFirst()
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("LiquidationWaterfall not found after create: " + waterfallId));
    }

    private Optional<CreatedEvent> findWaterfallEventByContractId(String partyId, String contractId) {
        return getActiveWaterfalls(partyId).stream()
                .filter(e -> e.getContractId().equals(contractId))
                .findFirst();
    }

    private List<CreatedEvent> getActiveWaterfalls(String partyId) {
        return ledgerCommandService.getActiveContracts(
                        partyId,
                        buildEventFormat(partyId)
                ).stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("LiquidationWaterfall"))
                .collect(Collectors.toList());
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

    private LiquidationWaterfallDto toDto(LiquidationWaterfall.Contract contract) {
        return new LiquidationWaterfallDto(
                contract.id.contractId,
                contract.data.primeBroker,
                contract.data.hedgeFund,
                contract.data.riskManager,
                contract.data.waterfallId,
                contract.data.marginCallCid.toString(),
                contract.data.poolCid.toString(),
                contract.data.originalShortfall.doubleValue(),
                contract.data.remainingShortfall.doubleValue(),
                List.of(),  // audit events fetched separately via getAuditEvents
                contract.data.status.name(),
                contract.data.initiatedAt != null ? contract.data.initiatedAt.toString() : null,
                contract.data.updatedAt != null ? contract.data.updatedAt.toString() : null
        );
    }

    private LiquidationAuditEventDto auditEventToDto(LiquidationAuditEvent.Contract contract) {
        return new LiquidationAuditEventDto(
                contract.id.contractId,
                contract.data.waterfallId,
                contract.data.primeBroker,
                contract.data.hedgeFund,
                contract.data.riskManager,
                contract.data.step.positionId,
                contract.data.step.assetClass,
                contract.data.step.grossValue.doubleValue(),
                contract.data.step.haircutAdj.doubleValue(),
                contract.data.step.appliedToShortfall.doubleValue(),
                contract.data.step.remainingShortfall.doubleValue(),
                contract.data.executedAt != null ? contract.data.executedAt.toString() : null
        );
    }
}