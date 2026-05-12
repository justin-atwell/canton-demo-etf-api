package com.canton.etf.api.service;

import com.canton.etf.common.ledger.LedgerCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RebalanceServiceTest {
    @Mock
    private LedgerCommandService ledgerCommandService;

    @InjectMocks
    private RebalanceService testObject;

    @Test
    public void testPropose() {
        //todo waiting until methods stubbed
    }
}
