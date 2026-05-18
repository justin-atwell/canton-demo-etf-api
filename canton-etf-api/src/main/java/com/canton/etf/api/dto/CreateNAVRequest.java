package com.canton.etf.api.dto;

import java.time.LocalDate;

public record CreateNAVRequest(
        LocalDate navDate,
        double navPerShare,
        double totalAUM,
        String source) {}