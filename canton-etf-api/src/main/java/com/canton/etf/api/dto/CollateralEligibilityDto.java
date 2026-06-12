package com.canton.etf.api.dto;

import java.math.BigDecimal;

public record CollateralEligibilityDto(
        String contractId,
        String primeBroker,
        String hedgeFund,
        String scheduleId,
        boolean acceptsTreasuries,
        boolean acceptsBitcoin,
        boolean acceptsMMF,
        double maxBtcConcentration,
        double maxTreasuryConc,
        double maxMmfConcentration,
        double minHaircutAdjValue
) { }