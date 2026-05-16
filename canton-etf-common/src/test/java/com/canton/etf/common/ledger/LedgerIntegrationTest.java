package com.canton.etf.common.ledger;

import com.daml.ledger.javaapi.data.Filter;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.junit.jupiter.api.Assertions.*;

// Only runs when -Dintegration=true is passed
// so it doesn't break CI when sandbox isn't running
@EnabledIfSystemProperty(named = "integration", matches = "true")
class LedgerIntegrationTest {

    private static final String FUND_MANAGER =
            "FundManager::1220654ea376a38d37c3e21afb99c8d73e613add19378040683dd778a4107f1ebee2";
    @Test
    void canConnectToSandbox() {
        var channel = ManagedChannelBuilder
                .forAddress("localhost", 6865)
                .usePlaintext()
                .build();

        var service = new LedgerCommandService(channel);

        // Query active ETFDefinition contracts — should be empty on a fresh sandbox
        // We're just proving the gRPC connection works
        var results = service.getActiveContracts(
                FUND_MANAGER,
                new com.daml.ledger.javaapi.data.EventFormat(
                        java.util.Map.of(
                                FUND_MANAGER,
                                new com.daml.ledger.javaapi.data.CumulativeFilter(
                                        java.util.Map.of(),
                                        java.util.Map.of(),
                                        java.util.Optional.of(
                                                Filter.Wildcard.HIDE_CREATED_EVENT_BLOB
                                        )
                                )
                        ),
                        java.util.Optional.empty(),
                        true
                )
        );

        assertNotNull(results);
        // Empty is correct — we haven't created any contracts yet
        assertEquals(0, results.size());

        channel.shutdown();
    }
}