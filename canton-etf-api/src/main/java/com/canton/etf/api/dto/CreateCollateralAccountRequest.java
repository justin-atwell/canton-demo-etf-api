package com.canton.etf.api.dto;

public record CreateCollateralAccountRequest(
        String custodian,
        String fundManager,
        String compliance,
        String auditor,
        String asset,
        double initialBalance,
        String accountId
) {}