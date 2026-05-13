package com.canton.etf.fix;

import com.canton.etf.common.ledger.LedgerCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix50sp2.ExecutionReport;
import quickfix.fix50sp2.NewOrderSingle;

@Component
public class CantonFixHandler implements Application {

    private static final Logger log = LoggerFactory.getLogger(CantonFixHandler.class);

    private final LedgerCommandService ledgerCommandService;

    public CantonFixHandler(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    @Override
    public void onCreate(SessionID sessionID) {
        log.info("FIX session created: {}", sessionID);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        log.info("FIX session logon: {}", sessionID);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        log.info("FIX session logout: {}", sessionID);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {}

    @Override
    public void fromAdmin(Message message, SessionID sessionID) {}

    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {}

    @Override
    public void fromApp(Message message, SessionID sessionID)
            throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

        String msgType = message.getHeader()
                .getString(MsgType.FIELD);

        if (MsgType.ORDER_SINGLE.equals(msgType)) {
            handleNewOrderSingle((NewOrderSingle) message, sessionID);
        }
    }

    private void handleNewOrderSingle(NewOrderSingle order, SessionID sessionID) {
        try {
            String symbol = order.getSymbol().getValue();
            char side = order.getSide().getValue();
            double qty = order.getOrderQty().getValue();

            log.info("Received NewOrderSingle - symbol: {} side: {} qty: {}",
                    symbol, side, qty);

            // TODO: create ExecutionReport contract on Canton ledger

            sendExecutionReport(symbol, side, qty, sessionID);

        } catch (FieldNotFound e) {
            log.error("Missing required field in NewOrderSingle", e);
        }
    }

    private void sendExecutionReport(String symbol, char side,
                                     double qty, SessionID sessionID) {
        try {
            ExecutionReport report = new ExecutionReport(
                    new OrderID("ETF-" + System.currentTimeMillis()),
                    new ExecID("EXEC-" + System.currentTimeMillis()),
                    new ExecType(ExecType.NEW),
                    new OrdStatus(OrdStatus.NEW),
                    new Side(side),
                    new LeavesQty(qty),
                    new CumQty(0)
            );
            report.set(new Symbol(symbol));
            Session.sendToTarget(report, sessionID);
            log.info("Sent ExecutionReport for {}", symbol);
        } catch (SessionNotFound e) {
            log.error("FIX session not found", e);
        }
    }
}