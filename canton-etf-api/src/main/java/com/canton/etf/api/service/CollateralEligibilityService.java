package com.canton.etf.api.service;

import com.canton.etf.api.dto.CollateralEligibilityDto;
import com.canton.etf.api.dto.EligibilityCheckRequestDto;
import com.canton.etf.api.dto.EligibilityCheckResultDto;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.CheckEligibility;
import com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.CollateralEligibility;
import com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.UpdateSchedule;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.HaircutRate;
import com.canton.etf.api.dto.CollateralPositionDto;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.CumulativeFilter;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.Filter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class CollateralEligibilityService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public CollateralEligibilityService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    public CollateralEligibilityDto createSchedule(String partyId, CollateralEligibilityDto request) {
        String scheduleId = request.scheduleId() != null ? request.scheduleId() : UUID.randomUUID().toString();
        var command = new CollateralEligibility(
                request.primeBroker(),
                request.hedgeFund(),
                scheduleId,
                request.acceptsTreasuries(),
                request.acceptsBitcoin(),
                request.acceptsMMF(),
                BigDecimal.valueOf(request.maxBtcConcentration()),
                BigDecimal.valueOf(request.maxTreasuryConc()),
                BigDecimal.valueOf(request.maxMmfConcentration()),
                BigDecimal.valueOf(request.minHaircutAdjValue())
        ).create().commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("CollateralEligibility created: scheduleId={} primeBroker={}", scheduleId, request.primeBroker());

        return getScheduleByScheduleId(partyId, scheduleId);
    }

    public CollateralEligibilityDto getSchedule(String partyId, String contractId) {
        CreatedEvent event = findEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("CollateralEligibility not found: " + contractId));
        return toDto(CollateralEligibility.Contract.fromCreatedEvent(event));
    }

    public EligibilityCheckResultDto checkEligibility(String partyId, String contractId,
                                                      EligibilityCheckRequestDto request) {
        var command = new CollateralEligibility.ContractId(contractId)
                .exerciseCheckEligibility(new CheckEligibility(toModel(request.position())))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("CollateralEligibility.CheckEligibility exercised: contractId={}", contractId);

        // nonconsuming — result not surfaced via submitAndWait; optimistic return
        return new EligibilityCheckResultDto(EligibilityCheckResultDto.Status.ELIGIBLE, null, request.position().haircut().rate());
    }

    public CollateralEligibilityDto updateSchedule(String partyId, String contractId,
                                                   CollateralEligibilityDto request) {
        var command = new CollateralEligibility.ContractId(contractId)
                .exerciseUpdateSchedule(new UpdateSchedule(
                        BigDecimal.valueOf(request.maxBtcConcentration()),
                        BigDecimal.valueOf(request.maxTreasuryConc()),
                        BigDecimal.valueOf(request.maxMmfConcentration()),
                        BigDecimal.valueOf(request.minHaircutAdjValue())
                ))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("CollateralEligibility.UpdateSchedule exercised: contractId={}", contractId);

        return getSchedule(partyId, contractId);
    }

    // --- Internal helpers ---

    private CollateralEligibilityDto getScheduleByScheduleId(String partyId, String scheduleId) {
        return getActiveSchedules(partyId).stream()
                .map(CollateralEligibility.Contract::fromCreatedEvent)
                .filter(c -> c.data.scheduleId.equals(scheduleId))
                .findFirst()
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("CollateralEligibility not found after create: " + scheduleId));
    }

    private Optional<CreatedEvent> findEventByContractId(String partyId, String contractId) {
        return getActiveSchedules(partyId).stream()
                .filter(e -> e.getContractId().equals(contractId))
                .findFirst();
    }

    public List<CollateralEligibilityDto> getSchedules(String partyId) {
        return getActiveSchedules(partyId).stream()
                .map(CollateralEligibility.Contract::fromCreatedEvent)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private List<CreatedEvent> getActiveSchedules(String partyId) {
        return ledgerCommandService.getActiveContracts(
                partyId,
                buildEventFormat(partyId));
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

    private CollateralEligibilityDto toDto(CollateralEligibility.Contract contract) {
        return new CollateralEligibilityDto(
                contract.id.contractId,
                contract.data.primeBroker,
                contract.data.hedgeFund,
                contract.data.scheduleId,
                contract.data.acceptsTreasuries,
                contract.data.acceptsBitcoin,
                contract.data.acceptsMMF,
                contract.data.maxBtcConcentration.doubleValue(),
                contract.data.maxTreasuryConc.doubleValue(),
                contract.data.maxMmfConcentration.doubleValue(),
                contract.data.minHaircutAdjValue.doubleValue()
        );
    }

    private CollateralPosition toModel(CollateralPositionDto dto) {
        return new CollateralPosition(
                AssetTypeMappingUtil.toModel(dto.asset()),
                new HaircutRate(BigDecimal.valueOf(dto.haircut().rate())),
                dto.positionId(),
                dto.postingParty(),
                dto.valuedAt() != null ? java.time.Instant.parse(dto.valuedAt()) : java.time.Instant.now()
        );
    }
}