package com.canton.etf.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record NBBOQuoteResponse(
        String symbol,
        BigDecimal bid,
        BigDecimal ask,
        Instant timestamp
) {}