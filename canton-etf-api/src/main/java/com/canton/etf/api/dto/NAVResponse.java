package com.canton.etf.api.dto;

import com.canton.etf.model.canton.etf.pricing.nav.NAV;

import java.math.BigDecimal;
import java.time.LocalDate;

public record NAVResponse(
        String contractId,
        String ticker,
        LocalDate navDate,
        BigDecimal navPerShare,
        BigDecimal totalAUM,
        String source,
        String fundManager) {

    public static NAVResponse from(NAV.Contract contract) {
        return new NAVResponse(
                contract.id.contractId,
                contract.data.ticker,
                contract.data.navDate,
                contract.data.navPerShare,
                contract.data.totalAUM,
                contract.data.source,
                contract.data.fundManager
        );
    }
}