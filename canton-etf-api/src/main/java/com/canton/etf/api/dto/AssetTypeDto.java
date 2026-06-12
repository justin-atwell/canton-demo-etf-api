package com.canton.etf.api.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AssetTypeDto.TokenizedTreasuryDto.class, name = "TREASURY"),
        @JsonSubTypes.Type(value = AssetTypeDto.BitcoinCollateralDto.class, name = "BTC"),
        @JsonSubTypes.Type(value = AssetTypeDto.MoneyMarketFundShareDto.class, name = "MMF")
})
public sealed interface AssetTypeDto permits
        AssetTypeDto.TokenizedTreasuryDto,
        AssetTypeDto.BitcoinCollateralDto,
        AssetTypeDto.MoneyMarketFundShareDto {

    String type();

    public record TokenizedTreasuryDto(
            String type,
            String cusip,
            String maturityDate,    // ISO date string — LocalDate on model side
            double faceValue,
            String issuer,
            double accruedInterest
    ) implements AssetTypeDto { }

    public record BitcoinCollateralDto(
            String type,
            String custodianRef,
            String walletAddress,
            double quantity,
            double spotPrice,
            double volatility30d
    ) implements AssetTypeDto { }

    public record MoneyMarketFundShareDto(
            String type,
            String fundName,
            String shareClass,
            double navPerShare,
            double quantity
    ) implements AssetTypeDto { }
}