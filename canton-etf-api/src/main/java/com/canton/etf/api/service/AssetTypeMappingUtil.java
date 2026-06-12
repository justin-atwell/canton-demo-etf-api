package com.canton.etf.api.service;

import com.canton.etf.api.dto.AssetTypeDto;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.AssetType;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.assettype.BitcoinCollateral;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.assettype.MoneyMarketFundShare;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.assettype.TokenizedTreasury;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AssetTypeMappingUtil {

    private AssetTypeMappingUtil() {}

    public static AssetTypeDto toDto(AssetType assetType) {
        if (assetType instanceof TokenizedTreasury t) {
            return new AssetTypeDto.TokenizedTreasuryDto(
                    "TREASURY",
                    t.cusip,
                    t.maturityDate.toString(),
                    t.faceValue.doubleValue(),
                    t.issuer,
                    t.accruedInterest.doubleValue()
            );
        } else if (assetType instanceof BitcoinCollateral b) {
            return new AssetTypeDto.BitcoinCollateralDto(
                    "BTC",
                    b.custodianRef,
                    b.walletAddress,
                    b.quantity.doubleValue(),
                    b.spotPrice.doubleValue(),
                    b.volatility30d.doubleValue()
            );
        } else if (assetType instanceof MoneyMarketFundShare m) {
            return new AssetTypeDto.MoneyMarketFundShareDto(
                    "MMF",
                    m.fundName,
                    m.shareClass,
                    m.navPerShare.doubleValue(),
                    m.quantity.doubleValue()
            );
        }
        throw new IllegalArgumentException("Unknown AssetType variant: " + assetType.getClass().getSimpleName());
    }

    public static AssetType toModel(AssetTypeDto dto) {
        if (dto instanceof AssetTypeDto.TokenizedTreasuryDto t) {
            return new TokenizedTreasury(
                    t.cusip(),
                    LocalDate.parse(t.maturityDate()),
                    BigDecimal.valueOf(t.faceValue()),
                    t.issuer(),
                    BigDecimal.valueOf(t.accruedInterest())
            );
        } else if (dto instanceof AssetTypeDto.BitcoinCollateralDto b) {
            return new BitcoinCollateral(
                    b.custodianRef(),
                    b.walletAddress(),
                    BigDecimal.valueOf(b.quantity()),
                    BigDecimal.valueOf(b.spotPrice()),
                    BigDecimal.valueOf(b.volatility30d())
            );
        } else if (dto instanceof AssetTypeDto.MoneyMarketFundShareDto m) {
            return new MoneyMarketFundShare(
                    m.fundName(),
                    m.shareClass(),
                    BigDecimal.valueOf(m.navPerShare()),
                    BigDecimal.valueOf(m.quantity())
            );
        }
        throw new IllegalArgumentException("Unknown AssetTypeDto variant: " + dto.getClass().getSimpleName());
    }
}