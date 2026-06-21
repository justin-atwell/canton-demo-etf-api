package com.canton.etf.api.service;

import com.canton.etf.api.dto.SubstitutionRequestDto;
import com.canton.etf.api.dto.HaircutRateDto;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.common.security.Auth0JwtValidator;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.HaircutRate;
import com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.ApproveByCustodian;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.ConfirmTransfer;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.RejectByBroker;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.RejectByCustodian;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionRequest;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionStatus;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus.ApprovedByBroker;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus.Completed;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus.Pending;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus.Rejected;
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
public class SubstitutionRequestService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;
    private final Auth0JwtValidator jwtValidator;

    public SubstitutionRequestService(LedgerCommandService ledgerCommandService, Auth0JwtValidator jwtValidator) {
        this.ledgerCommandService = ledgerCommandService;
        this.jwtValidator = jwtValidator;
    }

    public SubstitutionRequestDto createRequest(String partyId, SubstitutionRequestDto request) {
        String requestId = request.requestId() != null ? request.requestId() : UUID.randomUUID().toString();
        Instant now = Instant.now();

        var command = new SubstitutionRequest(
                request.hedgeFund(),
                request.primeBroker(),
                request.custodian(),
                request.riskManager(),
                requestId,
                request.outgoingPosId(),
                AssetTypeMappingUtil.toModel(request.outgoingAsset()),
                AssetTypeMappingUtil.toModel(request.incomingAsset()),
                new HaircutRate(BigDecimal.valueOf(request.incomingHaircut().rate())),
                new CollateralPool.ContractId(request.poolCid()),
                new Pending(com.daml.ledger.javaapi.data.Unit.getInstance()),
                now,
                now
        ).create().commands().getFirst();

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("SubstitutionRequest created: requestId={} hedgeFund={}", requestId, request.hedgeFund());

        return getRequestByRequestId(partyId, requestId);
    }

    public SubstitutionRequestDto getRequest(String partyId, String contractId) {
        CreatedEvent event = findEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("SubstitutionRequest not found: " + contractId));
        return toDto(SubstitutionRequest.Contract.fromCreatedEvent(event));
    }

    public SubstitutionRequestDto approveByCustodian(String partyId, String contractId, String comment) {
        CreatedEvent event = findEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("SubstitutionRequest not found: " + contractId));
        SubstitutionRequest.Contract contract = SubstitutionRequest.Contract.fromCreatedEvent(event);
        String requestId = contract.data.requestId;

        var command = new SubstitutionRequest.ContractId(contractId)
                .exerciseApproveByCustodian(new ApproveByCustodian(comment))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(
                List.of(partyId),
                List.of(contract.data.hedgeFund),
                APP_ID,
                List.of(command)
        );
        log.info("SubstitutionRequest.ApproveByCustodian exercised: contractId={}", contractId);

        // Re-fetch by requestId since contractId changed after consuming choice
        return getRequestByRequestId(partyId, requestId);
    }

    public List<SubstitutionRequestDto> getRequests(String partyId) {
        return getActiveRequests(partyId).stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("SubstitutionRequest"))
                .map(SubstitutionRequest.Contract::fromCreatedEvent)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public SubstitutionRequestDto rejectByBroker(String partyId, String contractId, String reason) {
        CreatedEvent event = findEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("SubstitutionRequest not found: " + contractId));
        SubstitutionRequest.Contract contract = SubstitutionRequest.Contract.fromCreatedEvent(event);

        var command = new SubstitutionRequest.ContractId(contractId)
                .exerciseRejectByBroker(new RejectByBroker(reason))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(
                List.of(partyId),
                List.of(contract.data.hedgeFund),
                APP_ID,
                List.of(command)
        );
        log.info("SubstitutionRequest.RejectByBroker exercised: contractId={}", contractId);

        return getRequest(partyId, contractId);
    }

    public SubstitutionRequestDto confirmTransfer(String partyId, String contractId) {
        CreatedEvent event = findEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("SubstitutionRequest not found: " + contractId));
        SubstitutionRequest.Contract contract = SubstitutionRequest.Contract.fromCreatedEvent(event);
        String requestId = contract.data.requestId;

        CollateralPosition incomingPosition = new CollateralPosition(
                contract.data.incomingAsset,
                contract.data.incomingHaircut,
                contract.data.outgoingPosId,
                contract.data.hedgeFund,
                Instant.now()
        );

        var command = new SubstitutionRequest.ContractId(contractId)
                .exerciseConfirmTransfer(new ConfirmTransfer(incomingPosition))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(
                List.of(partyId),
                List.of(contract.data.hedgeFund),
                APP_ID,
                List.of(command)
        );
        log.info("SubstitutionRequest.ConfirmTransfer exercised: contractId={}", contractId);

        // Re-f   etch by requestId since contractId changed after consuming choice
        return getRequestByRequestId(partyId, requestId);
    }

    public SubstitutionRequestDto rejectByCustodian(String partyId, String contractId, String reason) {
        CreatedEvent event = findEventByContractId(partyId, contractId)
                .orElseThrow(() -> new RuntimeException("SubstitutionRequest not found: " + contractId));
        SubstitutionRequest.Contract contract = SubstitutionRequest.Contract.fromCreatedEvent(event);

        var command = new SubstitutionRequest.ContractId(contractId)
                .exerciseRejectByCustodian(new RejectByCustodian(reason))
                .commands().getFirst();

        ledgerCommandService.submitAndWait(
                List.of(partyId),
                List.of(contract.data.hedgeFund),
                APP_ID,
                List.of(command)
        );
        log.info("SubstitutionRequest.RejectByCustodian exercised: contractId={}", contractId);

        return getRequest(partyId, contractId);
    }

    // --- Internal helpers ---

    private SubstitutionRequestDto getRequestByRequestId(String partyId, String requestId) {
        return getActiveRequests(partyId).stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("SubstitutionRequest"))
                .map(SubstitutionRequest.Contract::fromCreatedEvent)
                .filter(c -> c.data.requestId.equals(requestId))
                .findFirst()
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("SubstitutionRequest not found after create: " + requestId));
    }

    private Optional<CreatedEvent> findEventByContractId(String partyId, String contractId) {
        List<CreatedEvent> events = getActiveRequests(partyId);
        log.info("findEventByContractId: looking for={} found={} events", contractId, events.size());
        events.forEach(e -> log.info("  event contractId={} templateId={}", e.getContractId(), e.getTemplateId().getEntityName()));
        return events.stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("SubstitutionRequest"))
                .filter(e -> e.getContractId().equals(contractId))
                .findFirst();
    }

    private List<CreatedEvent> getActiveRequests(String partyId) {
        return ledgerCommandService.getActiveContracts(
                partyId,
                buildEventFormat(partyId)
        );
    }

    private EventFormat buildEventFormat(String partyId) {
        var filter = new CumulativeFilter(
                Map.of(), Map.of(),
                Optional.of(Filter.Wildcard.HIDE_CREATED_EVENT_BLOB)
        );
        Map<String, Filter> filters = new java.util.HashMap<>();
        for (String role : List.of("HedgeFund", "PrimeBroker", "Custodian", "RiskManager")) {
            filters.put(jwtValidator.getDevPartyId(role), filter);
        }
        return new EventFormat(filters, Optional.empty(), true);
    }

    // --- Mapping ---

    private SubstitutionRequestDto toDto(SubstitutionRequest.Contract contract) {
        String rejectionReason = contract.data.status instanceof Rejected r ? r.rejectionReason : null;
        return new SubstitutionRequestDto(
                contract.id.contractId,              // ← must be contract.id.getValue()
                contract.data.hedgeFund,
                contract.data.primeBroker,
                contract.data.custodian,
                contract.data.riskManager,
                contract.data.requestId,
                contract.data.outgoingPosId,
                AssetTypeMappingUtil.toDto(contract.data.outgoingAsset),
                AssetTypeMappingUtil.toDto(contract.data.incomingAsset),
                new HaircutRateDto(contract.data.incomingHaircut.rate.doubleValue()),
                contract.data.poolCid.contractId,   // ← .getValue() not .toString()
                mapStatus(contract.data.status),
                rejectionReason,
                contract.data.createdAt != null ? contract.data.createdAt.toString() : null,
                contract.data.updatedAt != null ? contract.data.updatedAt.toString() : null
        );
    }

    private SubstitutionRequestDto.Status mapStatus(SubstitutionStatus status) {
        if (status instanceof Pending) return SubstitutionRequestDto.Status.PENDING;
        if (status instanceof ApprovedByBroker) return SubstitutionRequestDto.Status.APPROVED_BY_BROKER;
        if (status instanceof Completed) return SubstitutionRequestDto.Status.COMPLETED;
        if (status instanceof Rejected) return SubstitutionRequestDto.Status.REJECTED;
        throw new IllegalArgumentException("Unknown SubstitutionStatus: " + status.getClass().getSimpleName());
    }
}