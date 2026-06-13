package com.canton.etf.api.dto;

import java.util.List;

public record LiquidationWaterfallDto(
        String contractId,
        String primeBroker,
        String hedgeFund,
        String riskManager,
        String waterfallId,
        String marginCallCid,
        String poolCid,
        double originalShortfall,
        double remainingShortfall,
        List<LiquidationAuditEventDto> auditEvents,
        String status,
        String initiatedAt,
        String updatedAt
) { }