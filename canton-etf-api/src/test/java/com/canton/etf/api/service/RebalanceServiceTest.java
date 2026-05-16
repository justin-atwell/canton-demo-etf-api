package com.canton.etf.api.service;

import com.canton.etf.api.dto.CreateRebalanceRequest;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.Identifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RebalanceServiceTest {

    @Mock
    private LedgerCommandService ledgerCommandService;

    private RebalanceService rebalanceService;

    private static final String FUND_MANAGER = "FundManager::abc123";
    private static final String COMPLIANCE   = "ComplianceOfficer::def456";
    private static final String TICKER       = "CXETH";
    private static final String PROPOSAL_ID  = "prop-2024-001";

    private static final CreateRebalanceRequest VALID_REQUEST = new CreateRebalanceRequest(
            PROPOSAL_ID,
            List.of(
                    new CreateRebalanceRequest.WeightEntry("ETH", 0.60),
                    new CreateRebalanceRequest.WeightEntry("BTC", 0.40)
            )
    );

    @BeforeEach
    void setUp() {
        rebalanceService = new RebalanceService(ledgerCommandService);
    }

    // -------------------------------------------------------------------------
    // propose — only testable path at unit level is the ETF-not-found branch.
    // Full happy path covered in RebalanceServiceIntegrationTest.
    // -------------------------------------------------------------------------

    @Test
    void propose_throwsWhenEtfNotFound() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rebalanceService.propose(FUND_MANAGER, TICKER, VALID_REQUEST));

        assertTrue(ex.getMessage().contains(TICKER));
    }

    @Test
    void propose_doesNotSubmitWhenEtfNotFound() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class,
                () -> rebalanceService.propose(FUND_MANAGER, TICKER, VALID_REQUEST));

        verify(ledgerCommandService, never()).submitAndWait(any(), any(), any());
    }

    // -------------------------------------------------------------------------
    // approve
    // -------------------------------------------------------------------------

    @Test
    void approve_throwsWhenProposalNotFound() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rebalanceService.approve(COMPLIANCE, TICKER, PROPOSAL_ID));

        assertTrue(ex.getMessage().contains(PROPOSAL_ID));
    }

    @Test
    void approve_doesNotSubmitWhenProposalNotFound() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class,
                () -> rebalanceService.approve(COMPLIANCE, TICKER, PROPOSAL_ID));

        verify(ledgerCommandService, never()).submitAndWait(any(), any(), any());
    }

    @Test
    void approve_ignoresContractsWithWrongEntityName() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of(mockEventWithEntityName("ETFDefinition")));

        assertThrows(RuntimeException.class,
                () -> rebalanceService.approve(COMPLIANCE, TICKER, PROPOSAL_ID));
    }

    // -------------------------------------------------------------------------
    // reject
    // -------------------------------------------------------------------------

    @Test
    void reject_throwsWhenProposalNotFound() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rebalanceService.reject(COMPLIANCE, TICKER, PROPOSAL_ID));

        assertTrue(ex.getMessage().contains(PROPOSAL_ID));
    }

    @Test
    void reject_doesNotSubmitWhenProposalNotFound() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class,
                () -> rebalanceService.reject(COMPLIANCE, TICKER, PROPOSAL_ID));

        verify(ledgerCommandService, never()).submitAndWait(any(), any(), any());
    }

    // -------------------------------------------------------------------------
    // execute
    // -------------------------------------------------------------------------

    @Test
    void execute_throwsWhenProposalNotFound() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> rebalanceService.execute(FUND_MANAGER, TICKER, PROPOSAL_ID));

        assertTrue(ex.getMessage().contains(PROPOSAL_ID));
    }

    @Test
    void execute_doesNotSubmitWhenProposalNotFound() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of());

        assertThrows(RuntimeException.class,
                () -> rebalanceService.execute(FUND_MANAGER, TICKER, PROPOSAL_ID));

        verify(ledgerCommandService, never()).submitAndWait(any(), any(), any());
    }

    @Test
    void execute_ignoresContractsWithWrongEntityName() {
        when(ledgerCommandService.getActiveContracts(any(), any(EventFormat.class)))
                .thenReturn(List.of(mockEventWithEntityName("ETFDefinition")));

        assertThrows(RuntimeException.class,
                () -> rebalanceService.execute(FUND_MANAGER, TICKER, PROPOSAL_ID));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private CreatedEvent mockEventWithEntityName(String entityName) {
        CreatedEvent event = mock(CreatedEvent.class, RETURNS_DEEP_STUBS);
        Identifier templateId = mock(Identifier.class);
        when(templateId.getEntityName()).thenReturn(entityName);
        when(event.getTemplateId()).thenReturn(templateId);
        return event;
    }
}