package com.canton.etf.api.dto;

import com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition;

import java.time.LocalDate;
import java.util.List;

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
        LocalDate inceptionDate,
        List<ConstituentResponse> constituents
) {
    public static EtfResponse from(ETFDefinition.Contract contract, List<ConstituentResponse> constituents) {
        return new EtfResponse(
                contract.id.contractId,
                contract.data.ticker,
                contract.data.name,
                contract.data.cusip,
                contract.data.status,
                contract.data.fundManager,
                contract.data.custodian,
                contract.data.compliance,
                contract.data.auditor,
                contract.data.inceptionDate,
                constituents
        );
    }
}