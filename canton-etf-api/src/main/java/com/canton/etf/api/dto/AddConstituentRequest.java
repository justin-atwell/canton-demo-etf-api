package com.canton.etf.api.dto;

import java.math.BigDecimal;

public record AddConstituentRequest(
        String symbol,
        String name,
        String cusip,
        BigDecimal weight
) {}