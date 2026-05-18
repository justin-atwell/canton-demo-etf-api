package com.canton.etf.api.dto;

public record MarginCallRequest(
        String accountId,
        String asset,
        double amountRequired,
        String dueBy
) {}