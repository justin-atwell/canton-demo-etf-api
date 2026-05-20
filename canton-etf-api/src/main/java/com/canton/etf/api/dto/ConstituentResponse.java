package com.canton.etf.api.dto;

import com.canton.etf.model.canton.etf.fund.constituent.Constituent;
import java.math.BigDecimal;

public record ConstituentResponse(
        String contractId,
        String ticker,
        String symbol,
        String name,
        String cusip,
        BigDecimal weight
) {
    public static ConstituentResponse from(Constituent.Contract contract) {
        return new ConstituentResponse(
                contract.id.contractId,
                contract.data.ticker,
                contract.data.symbol,
                contract.data.name,
                contract.data.cusip,
                contract.data.weight
        );
    }
}