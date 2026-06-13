package com.canton.etf.api.dto;

public record LiquidationAuditEventDto(
        String contractId,
        String waterfallId,
        String primeBroker,
        String hedgeFund,
        String riskManager,
        String positionId,
        String assetClass,
        double grossValue,
        double haircutAdj,
        double appliedToShortfall,
        double remainingShortfall,
        String executedAt
) { }