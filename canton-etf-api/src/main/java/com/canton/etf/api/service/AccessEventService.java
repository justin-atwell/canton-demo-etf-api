package com.canton.etf.api.service;

import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.iam.accessevent.AccessEvent;
import com.daml.ledger.javaapi.data.CumulativeFilter;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.Filter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class AccessEventService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;
    private final String operatorPartyId;

    public AccessEventService(
            LedgerCommandService ledgerCommandService,
            @Value("${canton.operator.party-id}") String operatorPartyId) {
        this.ledgerCommandService = ledgerCommandService;
        this.operatorPartyId = operatorPartyId;
    }

    // -------------------------------------------------------------------------
    // record
    //   Creates an immutable AccessEvent contract on the Canton ledger.
    //   Called after every significant ledger action across all services.
    //   Signatory: operator (sandbox party)
    //   Observer: actor
    // -------------------------------------------------------------------------
    public void record(
            String actorPartyId,
            String auditorPartyId,
            String action,
            String resource,
            boolean granted) {

        try {
            var command = new AccessEvent(
                    operatorPartyId,
                    actorPartyId,
                    auditorPartyId,
                    action,
                    resource,
                    Instant.now(),
                    granted,
                    "127.0.0.1",
                    UUID.randomUUID().toString()
            ).create().commands().get(0);

            ledgerCommandService.submitAndWait(operatorPartyId, APP_ID, List.of(command));
            log.debug("AccessEvent recorded: actor={} action={} resource={} granted={}",
                    actorPartyId, action, resource, granted);
        } catch (Exception e) {
            // Never let access event failures block the main operation
            log.warn("Failed to record AccessEvent: actor={} action={} resource={} — {}",
                    actorPartyId, action, resource, e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // getEvents
    //   Returns all AccessEvent contracts visible to the party.
    //   Auditors see all; others see only their own.
    // -------------------------------------------------------------------------
    public List<AccessEvent.Contract> getEvents(String partyId) {
        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("AccessEvent"))
                .map(AccessEvent.Contract::fromCreatedEvent)
                .toList();
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