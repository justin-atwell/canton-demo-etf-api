package com.canton.etf.api.service;

import com.canton.etf.api.dto.CreateRebalanceRequest;
import com.canton.etf.api.dto.RebalanceProposalResponse;
import com.canton.etf.common.ledger.LedgerCommandService;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for RebalanceService against a live Canton sandbox.
 *
 * Run with: ./gradlew test -Dintegration=true
 *
 * Prerequisites:
 *   1. Start sandbox from canton-demo-etf-daml/:
 *        daml start --on-start "daml ledger allocate-parties --host localhost --port 6865 \
 *          FundManager Custodian ComplianceOfficer Auditor MarketMaker"
 *
 *   2. Update FUND_MANAGER and COMPLIANCE_PARTY below with party IDs
 *      printed by daml start (they change on every restart).
 *
 *   3. A CXETH ETFDefinition contract must exist on the ledger before running.
 *      POST /etf or run EtfServiceIntegrationTest first.
 *
 * Tests run in @Order sequence — each step depends on ledger state from the previous.
 */
@EnabledIfSystemProperty(named = "integration", matches = "true")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RebalanceServiceIntegrationTest {

    // Update after each `daml start`
    private static final String FUND_MANAGER     = "FundManager::REPLACE_ME";
    private static final String COMPLIANCE_PARTY = "ComplianceOfficer::REPLACE_ME";

    private static final String TICKER      = "CXETH";
    private static final String PROPOSAL_ID = "integ-prop-001";

    private static io.grpc.ManagedChannel channel;
    private static RebalanceService rebalanceService;

    private static final CreateRebalanceRequest REBALANCE_REQUEST = new CreateRebalanceRequest(
            PROPOSAL_ID,
            List.of(
                    new CreateRebalanceRequest.WeightEntry("ETH", 0.65),
                    new CreateRebalanceRequest.WeightEntry("BTC", 0.35)
            )
    );

    @BeforeAll
    static void connect() {
        channel = ManagedChannelBuilder
                .forAddress("localhost", 6865)
                .usePlaintext()
                .build();

        rebalanceService = new RebalanceService(new LedgerCommandService(channel));
    }

    @AfterAll
    static void disconnect() {
        if (channel != null) channel.shutdownNow();
    }

    // -------------------------------------------------------------------------
    // Happy path — full lifecycle in order
    // -------------------------------------------------------------------------

    @Test
    @Order(1)
    void propose_createsProposalOnLedger() {
        String result = rebalanceService.propose(FUND_MANAGER, TICKER, REBALANCE_REQUEST);
        assertEquals(PROPOSAL_ID, result);
    }

    @Test
    @Order(2)
    void getProposal_returnsCreatedProposal() {
        RebalanceProposalResponse response = rebalanceService.getProposal(
                FUND_MANAGER, TICKER, PROPOSAL_ID);

        assertEquals(PROPOSAL_ID, response.proposalId());
        assertEquals(TICKER, response.ticker());
        assertEquals("Pending", response.status());
        assertEquals(2, response.newWeights().size());
    }

    @Test
    @Order(3)
    void approve_transitionsStatusToApproved() {
        rebalanceService.approve(COMPLIANCE_PARTY, TICKER, PROPOSAL_ID);

        RebalanceProposalResponse response = rebalanceService.getProposal(
                COMPLIANCE_PARTY, TICKER, PROPOSAL_ID);

        assertEquals("Approved", response.status());
    }

    @Test
    @Order(4)
    void execute_archivesProposalAndCreatesExecution() {
        rebalanceService.execute(FUND_MANAGER, TICKER, PROPOSAL_ID);

        // Proposal archived — should no longer be in ACS
        assertThrows(RuntimeException.class, () ->
                rebalanceService.getProposal(FUND_MANAGER, TICKER, PROPOSAL_ID));
    }

    // -------------------------------------------------------------------------
    // Rejection path — separate proposal ID so it doesn't collide with above
    // -------------------------------------------------------------------------

    @Test
    @Order(5)
    void reject_transitionsStatusToRejected() {
        String rejectProposalId = "integ-prop-reject-001";
        CreateRebalanceRequest rejectRequest = new CreateRebalanceRequest(
                rejectProposalId,
                List.of(new CreateRebalanceRequest.WeightEntry("ETH", 1.0))
        );

        rebalanceService.propose(FUND_MANAGER, TICKER, rejectRequest);
        rebalanceService.reject(COMPLIANCE_PARTY, TICKER, rejectProposalId);

        RebalanceProposalResponse response = rebalanceService.getProposal(
                COMPLIANCE_PARTY, TICKER, rejectProposalId);

        assertEquals("Rejected", response.status());
    }

    // -------------------------------------------------------------------------
    // Guard test — execute without approve should throw
    // -------------------------------------------------------------------------

    @Test
    @Order(6)
    void execute_throwsWhenProposalNotApproved() {
        String pendingProposalId = "integ-prop-pending-001";
        CreateRebalanceRequest pendingRequest = new CreateRebalanceRequest(
                pendingProposalId,
                List.of(new CreateRebalanceRequest.WeightEntry("ETH", 1.0))
        );

        rebalanceService.propose(FUND_MANAGER, TICKER, pendingRequest);

        assertThrows(IllegalStateException.class, () ->
                rebalanceService.execute(FUND_MANAGER, TICKER, pendingProposalId));
    }
}