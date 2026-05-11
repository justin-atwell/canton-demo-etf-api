package com.canton.etf.api.dto;

public record CreateEtfRequest(
        String ticker,
        String name,
        String cusip,
        String custodian,
        String compliance,
        String auditor
) {}