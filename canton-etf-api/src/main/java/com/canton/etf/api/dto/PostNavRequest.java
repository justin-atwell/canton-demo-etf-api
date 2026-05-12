package com.canton.etf.api.dto;

public record PostNavRequest(
        String ticker,
        double navPerShare,
        double totalAUM,
        String source,
        String navDate
) {}