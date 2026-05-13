package com.canton.etf.fix;

import com.canton.etf.common.ledger.LedgerCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import quickfix.SessionID;

@ExtendWith(MockitoExtension.class)
class CantonFixHandlerTest {

    @Mock
    private LedgerCommandService ledgerCommandService;

    @InjectMocks
    private CantonFixHandler fixHandler;

    @Test
    void onCreate_doesNotThrow() {
        var sessionId = new SessionID("FIXT.1.1", "CANTON_ETF", "MARKET_MAKER");
        fixHandler.onCreate(sessionId);
    }

    @Test
    void onLogon_doesNotThrow() {
        var sessionId = new SessionID("FIXT.1.1", "CANTON_ETF", "MARKET_MAKER");
        fixHandler.onLogon(sessionId);
    }

    @Test
    void onLogout_doesNotThrow() {
        var sessionId = new SessionID("FIXT.1.1", "CANTON_ETF", "MARKET_MAKER");
        fixHandler.onLogout(sessionId);
    }
}