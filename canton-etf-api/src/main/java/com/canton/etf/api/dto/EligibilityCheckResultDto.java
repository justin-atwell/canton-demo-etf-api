package com.canton.etf.api.dto;

public record EligibilityCheckResultDto(
        Status status,
        String reason,          // populated when INELIGIBLE
        double appliedHaircut   // haircut rate that would apply if eligible
) {
    public enum Status { ELIGIBLE, INELIGIBLE }
}