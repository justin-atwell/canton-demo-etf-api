package com.canton.etf.api.service;

import com.canton.etf.api.dto.CreateEtfRequest;
import com.canton.etf.common.ledger.LedgerCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class EtfServiceTest {

    @Mock
    private LedgerCommandService ledgerCommandService;

    @InjectMocks
    private EtfService testObject;

    @Test
    void createEtf_returnsTickerOnSuccess() {
        var request = new CreateEtfRequest(
                "SPY", "S&P 500", "33333BB",
                "Custodian::abc123",
                "Compliance::abc123",
                "Auditor::abc123");

        when(ledgerCommandService.submitAndWait(any(), any(), any()))
                .thenReturn("cmd-123");

        var result = testObject.createEtf("FundManager::abc123", request);

        assertEquals("SPY", result);
    }

    @Test
    void createEtf_callsLedgerWithParty(){
        var request = new CreateEtfRequest(
                "SPY", "S&P 500", "33333BB",
                "Custodian::abc123",
                "Compliance::abc123",
                "Auditor::abc123");

        when(ledgerCommandService.submitAndWait(any(), any(), any()))
                .thenReturn("cmd-123");

        var result = testObject.createEtf("FundManager::abc123", request);
        verify(ledgerCommandService).submitAndWait(eq("FundManager::abc123"), any(), any());
    }

    @Test
    void createEtf_ledgerThrowsException(){
        var request = new CreateEtfRequest(
                "SPY", "S&P 500", "33333BB",
                "Custodian::abc123",
                "Compliance::abc123",
                "Auditor::abc123");

        when(ledgerCommandService.submitAndWait(any(), any(), any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> testObject.createEtf("FundManager::abc123", request));
    }
}
