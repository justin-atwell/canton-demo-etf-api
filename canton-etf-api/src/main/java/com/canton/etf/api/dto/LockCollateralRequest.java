package com.canton.etf.api.dto;

public record LockCollateralRequest(
        String accountId,
        double amount,
        String reason,
        String expiry
) {}