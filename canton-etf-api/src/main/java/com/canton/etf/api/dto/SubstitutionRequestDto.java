package com.canton.etf.api.dto;

public record SubstitutionRequestDto(
        String contractId,
        String hedgeFund,
        String primeBroker,
        String custodian,
        String riskManager,
        String requestId,
        String outgoingPosId,
        AssetTypeDto outgoingAsset,
        AssetTypeDto incomingAsset,
        HaircutRateDto incomingHaircut,
        String poolCid,             // CollateralPool contract ID
        Status status,
        String rejectionReason,
        String createdAt,
        String updatedAt
) {
    public enum Status { PENDING, APPROVED_BY_BROKER, COMPLETED, REJECTED }
}