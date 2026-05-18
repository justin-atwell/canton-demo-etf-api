package com.canton.etf.api.dto;

import com.canton.etf.model.canton.etf.collateral.collaterallock.CollateralLock;

import java.math.BigDecimal;
import java.time.Instant;

public record CollateralLockResponse(
        String contractId,
        String custodian,
        String fundManager,
        String asset,
        BigDecimal amount,
        String reason,
        Instant expiry,
        Instant lockedAt) {

    public static CollateralLockResponse from(CollateralLock.Contract contract) {
        return new CollateralLockResponse(
                contract.id.contractId,
                contract.data.custodian,
                contract.data.fundManager,
                contract.data.asset,
                contract.data.amount,
                contract.data.reason,
                contract.data.expiry,
                contract.data.lockedAt
        );
    }
}