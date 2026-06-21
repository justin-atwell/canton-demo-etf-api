package com.canton.etf.api.service;

import com.canton.etf.api.dto.CollateralPoolDto;
import com.canton.etf.api.dto.CollateralPositionDto;
import com.canton.etf.api.dto.HaircutRateDto;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.HaircutRate;
import com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool;
import com.canton.etf.model.canton.etf.primebrokerage.collateralpool.RevaluePool;
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
public class CollateralPoolService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public CollateralPoolService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    public CollateralPoolDto createPool(String partyId, CollateralPoolDto request) {
        String poolId = UUID.randomUUID().toString();
        var command = new CollateralPool(
                request.hedgeFund(),
                request.custodian(),
                request.riskManager(),
                poolId,
                List.of(),
                BigDecimal.ZERO,
                Instant.now()
        ).create().commands().getFirst();

        log.info("createPool actAs parties: {}", List.of(request.hedgeFund(), request.custodian()));

        ledgerCommandService.submitAndWait(
                List.of(request.hedgeFund(), request.custodian()),
                APP_ID,
                List.of(command)
        );

        log.info("CollateralPool created: poolId={}", poolId);

        return getPoolByPoolId(partyId, poolId);
    }

    public CollateralPoolDto getPool(String partyId, String contractId) {
        CreatedEvent event = findEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("CollateralPool not found: " + contractId));
        return toDto(CollateralPool.Contract.fromCreatedEvent(event));
    }

    public CollateralPoolDto addPosition(String partyId, String contractId, CollateralPositionDto position) {
        // Get the poolId before exercising — we'll need it to re-fetch after
        CreatedEvent event = findEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("CollateralPool not found: " + contractId));
        String poolId = CollateralPool.Contract.fromCreatedEvent(event).data.poolId;

        var command = new CollateralPool.ContractId(contractId)
                .exerciseAddPosition(toModel(position))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("CollateralPool.AddPosition exercised: contractId={}", contractId);

        // Re-fetch by poolId since contractId changed after the consuming choice
        return getPoolByPoolId(partyId, poolId);
    }
    public CollateralPoolDto removePosition(String partyId, String contractId, String positionId) {
        var command = new CollateralPool.ContractId(contractId)
                .exerciseRemovePosition(positionId)
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("CollateralPool.RemovePosition exercised: contractId={} positionId={}", contractId, positionId);

        return getPool(partyId, contractId);
    }

    public CollateralPoolDto revaluePool(String partyId, String contractId) {
        var command = new CollateralPool.ContractId(contractId)
                .exerciseRevaluePool(new RevaluePool(getPoolPositions(partyId, contractId)))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("CollateralPool.RevaluePool exercised: contractId={}", contractId);

        return getPool(partyId, contractId);
    }

    // --- Internal helpers ---

    private CollateralPoolDto getPoolByPoolId(String partyId, String poolId) {
        return getActivePools(partyId).stream()
                .map(CollateralPool.Contract::fromCreatedEvent)
                .filter(c -> c.data.poolId.equals(poolId))
                .findFirst()
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("CollateralPool not found after create: " + poolId));
    }

    private Optional<CreatedEvent> findEventByContractId(String partyId, String contractId) {
        return getActivePools(partyId).stream()
                .filter(e -> e.getContractId().equals(contractId))
                .findFirst();
    }

    private List<CollateralPosition> getPoolPositions(String partyId, String contractId) {
        return getActivePools(partyId).stream()
                .filter(e -> e.getContractId().equals(contractId))
                .map(CollateralPool.Contract::fromCreatedEvent)
                .findFirst()
                .map(c -> c.data.positions)
                .orElseThrow(() -> new RuntimeException("CollateralPool not found: " + contractId));
    }

    private List<CreatedEvent> getActivePools(String partyId) {
        return ledgerCommandService.getActiveContracts(
                        partyId,
                        buildEventFormat(partyId)
                ).stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("CollateralPool"))
                .collect(Collectors.toList());
    }

    public List<CollateralPoolDto> getPools(String partyId) {
        return getActivePools(partyId).stream()
                .map(CollateralPool.Contract::fromCreatedEvent)
                .map(this::toDto)
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

    private CollateralPoolDto toDto(CollateralPool.Contract contract) {
        return new CollateralPoolDto(
                contract.id.contractId,
                contract.data.hedgeFund,
                contract.data.custodian,
                contract.data.riskManager,
                contract.data.positions.stream().map(this::positionToDto).collect(Collectors.toList()),
                contract.data.totalValue.doubleValue(),
                contract.data.lastUpdated != null ? contract.data.lastUpdated.toString() : null
        );
    }

    private CollateralPositionDto positionToDto(CollateralPosition pos) {
        return new CollateralPositionDto(
                pos.positionId,
                pos.postingParty,
                AssetTypeMappingUtil.toDto(pos.asset),
                new HaircutRateDto(pos.haircut.rate.doubleValue()),
                pos.valuedAt != null ? pos.valuedAt.toString() : null
        );
    }

    private CollateralPosition toModel(CollateralPositionDto dto) {
        return new CollateralPosition(
                AssetTypeMappingUtil.toModel(dto.asset()),
                new HaircutRate(BigDecimal.valueOf(dto.haircut().rate())),
                dto.positionId(),
                dto.postingParty(),
                dto.valuedAt() != null ? Instant.parse(dto.valuedAt()) : Instant.now()
        );
    }
}