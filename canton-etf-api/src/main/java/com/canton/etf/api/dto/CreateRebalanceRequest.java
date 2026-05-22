package com.canton.etf.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record CreateRebalanceRequest(
        String proposalId,
        List<WeightEntry> newWeights
) {
    public record WeightEntry(String symbol, BigDecimal weight) {}
}