package com.canton.etf.api.dto;

import com.canton.etf.model.canton.etf.collateral.collateralaccount.CollateralAccount;

import java.math.BigDecimal;

public record CollateralAccountResponse(
        String contractId,
        String accountId,
        String custodian,
        String fundManager,
        String compliance,
        String auditor,
        String asset,
        BigDecimal balance) {

    public static CollateralAccountResponse from(CollateralAccount.Contract contract) {
        return new CollateralAccountResponse(
                contract.id.contractId,
                contract.data.accountId,
                contract.data.custodian,
                contract.data.fundManager,
                contract.data.compliance,
                contract.data.auditor,
                contract.data.asset,
                contract.data.balance
        );
    }
}