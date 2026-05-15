package com.canton.etf.common.ledger;

import com.daml.ledger.api.v2.CommandServiceGrpc;
import com.daml.ledger.api.v2.StateServiceGrpc;
import com.daml.ledger.api.v2.StateServiceOuterClass.GetActiveContractsResponse;
import com.daml.ledger.api.v2.StateServiceOuterClass.ActiveContract;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.codegen.ContractTypeCompanion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LedgerCommandServiceTest {

    @Mock
    private CommandServiceGrpc.CommandServiceBlockingStub commandStub;

    @Mock
    private StateServiceGrpc.StateServiceBlockingStub stateStub;

    @Mock
    private ContractTypeCompanion<?, ?, ?, ?> companion;

    private LedgerCommandService service;

    @BeforeEach
    void setUp() {
        service = new LedgerCommandService(commandStub, stateStub);
    }

    @Test
    void getActiveContracts_returnsEmptyList_whenLedgerReturnsNoContracts() {
        // Arrange
        var eventFormat = new EventFormat(
                Map.of(),           // partyToFilters — empty, we're testing the plumbing not the filter
                Optional.empty(),   // anyPartyFilter
                true                // verbose
        );

        var emptyResponse = GetActiveContractsResponse.newBuilder().build();
        Iterator<GetActiveContractsResponse> iterator = List.of(emptyResponse).iterator();
        when(stateStub.getActiveContracts(any())).thenReturn(iterator);

        // Act
        var result = service.getActiveContracts("FundManager::canton-demo::001", eventFormat);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void getActiveContracts_returnsContract_whenLedgerReturnsActiveContract() {
        // Arrange
        var eventFormat = new EventFormat(
                Map.of(),
                Optional.empty(),
                true
        );

        var createdEvent = com.daml.ledger.api.v2.EventOuterClass.CreatedEvent
                .newBuilder()
                .setContractId("canton::etf::001")
                .setTemplateId(
                        com.daml.ledger.api.v2.ValueOuterClass.Identifier.newBuilder()
                                .setPackageId("canton-demo-etf-0.1.0")
                                .setModuleName("Canton.Etf.Fund")
                                .setEntityName("ETFDefinition")
                                .build()
                )
                .build();

        var activeContract = ActiveContract.newBuilder()
                .setCreatedEvent(createdEvent)
                .build();

        var response = GetActiveContractsResponse.newBuilder()
                .setActiveContract(activeContract)
                .build();

        Iterator<GetActiveContractsResponse> iterator = List.of(response).iterator();
        when(stateStub.getActiveContracts(any())).thenReturn(iterator);

        // Act
        var result = service.getActiveContracts("FundManager::canton-demo::001", eventFormat);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("canton::etf::001", result.get(0).getContractId());
    }

    @Test
    void getActiveContracts_returnsMultipleContracts_whenLedgerReturnsMultiplePages() {

        var eventFormat = new EventFormat(
                Map.of(),
                Optional.empty(),
                true
        );
        // Arrange — two response pages, one contract each
        // This tests that our forEachRemaining correctly accumulates across pages
        var event1 = com.daml.ledger.api.v2.EventOuterClass.CreatedEvent
                .newBuilder().setContractId("contract::001")
                .setTemplateId(
                        com.daml.ledger.api.v2.ValueOuterClass.Identifier.newBuilder()
                                .setPackageId("canton-demo-etf-0.1.0")
                                .setModuleName("Canton.Etf.Fund")
                                .setEntityName("ETFDefinition")
                                .build()
                ).build();
        var event2 = com.daml.ledger.api.v2.EventOuterClass.CreatedEvent
                .newBuilder().setContractId("contract::002")
                .setTemplateId(
                        com.daml.ledger.api.v2.ValueOuterClass.Identifier.newBuilder()
                                .setPackageId("canton-demo-etf-0.1.0")
                                .setModuleName("Canton.Etf.Fund")
                                .setEntityName("ETFDefinition")
                                .build()
                ).build();

        var response1 = GetActiveContractsResponse.newBuilder()
                .setActiveContract(ActiveContract.newBuilder().setCreatedEvent(event1).build())
                .build();
        var response2 = GetActiveContractsResponse.newBuilder()
                .setActiveContract(ActiveContract.newBuilder().setCreatedEvent(event2).build())
                .build();

        Iterator<GetActiveContractsResponse> iterator = List.of(response1, response2).iterator();
        when(stateStub.getActiveContracts(any())).thenReturn(iterator);

        // Act
        var result = service.getActiveContracts("FundManager::canton-demo::001", eventFormat);

        // Assert
        assertEquals(2, result.size());
        assertEquals("contract::001", result.get(0).getContractId());
        assertEquals("contract::002", result.get(1).getContractId());
    }
}