package com.canton.etf.api.dto;

public record LockCollateralRequest(
        double amount,
        String reason,
        String expiry
) {}