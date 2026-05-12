package com.canton.etf.api.dto;

import java.util.List;

public record CreateRebalanceRequest(
        String proposalId,
        List<WeightEntry> newWeights
) {
    public record WeightEntry(String symbol, double weight) {}
}