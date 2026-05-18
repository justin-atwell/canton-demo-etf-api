package com.canton.etf.api.dto;

import com.canton.etf.model.canton.etf.collateral.margincall.MarginCall;

import java.math.BigDecimal;
import java.time.Instant;

public record MarginCallResponse(
        String contractId,
        String custodian,
        String fundManager,
        String compliance,
        String auditor,
        String asset,
        BigDecimal amountRequired,
        Instant issuedAt,
        Instant dueBy,
        String status) {

    public static MarginCallResponse from(MarginCall.Contract contract) {
        return new MarginCallResponse(
                contract.id.contractId,
                contract.data.custodian,
                contract.data.fundManager,
                contract.data.compliance,
                contract.data.auditor,
                contract.data.asset,
                contract.data.amountRequired,
                contract.data.issuedAt,
                contract.data.dueBy,
                contract.data.status
        );
    }
}