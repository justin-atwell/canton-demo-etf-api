package com.canton.etf.api.dto;

public record MarginCallRequest(
        String asset,
        double amountRequired,
        String dueBy
) {}