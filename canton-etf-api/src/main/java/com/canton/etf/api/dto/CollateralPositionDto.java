package com.canton.etf.api.dto;

public record CollateralPositionDto(
        String positionId,
        String postingParty,
        AssetTypeDto asset,
        HaircutRateDto haircut,
        String valuedAt     // Instant as ISO string
) { }