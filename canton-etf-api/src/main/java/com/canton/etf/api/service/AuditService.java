package com.canton.etf.api.service;

import com.canton.etf.api.dto.AccessEventResponse;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.iam.accessevent.AccessEvent;
import com.daml.ledger.javaapi.data.CumulativeFilter;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.Filter;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class AuditService {

    private static final String APP_ID = "canton-etf-api";
    private static final String AUDITOR_ROLE = "Auditor";

    private final LedgerCommandService ledgerCommandService;

    public AuditService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    // -------------------------------------------------------------------------
    // getAccessEvents
    //   Auditors see all events.
    //   All other roles see only events where actor matches their partyId.
    //   Sorted by timestamp descending — most recent first.
    // -------------------------------------------------------------------------
    public List<AccessEventResponse> getAccessEvents(String partyId) {
        log.debug("getAccessEvents: party={}", partyId);

        boolean isAuditor = partyId.startsWith(AUDITOR_ROLE + "::");

        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("AccessEvent"))
                .map(AccessEvent.Contract::fromCreatedEvent)
                .filter(c -> isAuditor || c.data.actor.equals(partyId))
                .map(AccessEventResponse::from)
                .sorted(Comparator.comparing(AccessEventResponse::timestamp).reversed())
                .toList();
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

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