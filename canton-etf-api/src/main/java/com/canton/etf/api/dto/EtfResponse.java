package com.canton.etf.api.dto;

import com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition;

import java.time.LocalDate;

public record EtfResponse(
        String contractId,
        String ticker,
        String name,
        String cusip,
        String status,
        String fundManager,
        String custodian,
        String compliance,
        String auditor,
        LocalDate inceptionDate
) {
    // Factory method — converts directly from the ledger contract
    public static EtfResponse from(ETFDefinition.Contract contract) {
        return new EtfResponse(
                contract.id.contractId,       // ledger contract ID as string
                contract.data.ticker,
                contract.data.name,
                contract.data.cusip,
                contract.data.status,
                contract.data.fundManager,
                contract.data.custodian,
                contract.data.compliance,
                contract.data.auditor,
                contract.data.inceptionDate
        );
    }
}