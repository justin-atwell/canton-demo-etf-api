package com.canton.etf.api.dto;

import java.util.List;

public record CollateralPoolDto(
        String contractId,
        String hedgeFund,
        String custodian,
        String riskManager,
        List<CollateralPositionDto> positions,
        double totalValue,
        String lastRevaluedAt
) { }