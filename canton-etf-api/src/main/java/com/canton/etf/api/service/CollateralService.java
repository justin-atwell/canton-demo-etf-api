package com.canton.etf.api.service;

import com.canton.etf.api.dto.CollateralTransactionRequest;
import com.canton.etf.api.dto.CreateCollateralAccountRequest;
import com.canton.etf.api.dto.LockCollateralRequest;
import com.canton.etf.api.dto.MarginCallRequest;
import com.canton.etf.common.ledger.LedgerCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CollateralService {
    private static final Logger log = LoggerFactory.getLogger(CollateralService.class);
    private final LedgerCommandService ledgerCommandService;

    public CollateralService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    public String createAccount(String partyId, CreateCollateralAccountRequest request) {
        return "collateral-stub";
    }

    public void deposit(String partyId, CollateralTransactionRequest request) {

    }

    public void lock(String partyId, LockCollateralRequest request) {

    }

    public void withdraw(String partyId, CollateralTransactionRequest request) {

    }

    public void issueMarginCall(String partyId, MarginCallRequest request) {

    }

    public void meetMarginCall(String partyId, String callId, MarginCallRequest request) {

    }

    public void defaultMarginCall(String partyId, String callId, MarginCallRequest request) {

    }
}
