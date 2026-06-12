package com.canton.etf.api.dto;

public record MarginCallV2Dto(
        String contractId,
        String primeBroker,
        String hedgeFund,
        String riskManager,
        String callId,
        String poolCid,
        double requiredCoverage,
        double currentCoverage,
        double shortfall,
        String responseDeadline,    // Instant as ISO string
        Status status,
        String issuedAt,
        String updatedAt
) {
    public enum Status { ISSUED, RESPONSERECEIVED, SATISFIED, DEFAULTED }
}