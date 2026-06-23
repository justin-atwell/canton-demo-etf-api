package com.canton.etf.model.canton.etf.primebrokerage.collateralasset;

import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.assettype.BitcoinCollateral;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.assettype.MoneyMarketFundShare;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.assettype.TokenizedTreasury;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.Value;
import com.daml.ledger.javaapi.data.codegen.PrimitiveValueDecoders;
import com.daml.ledger.javaapi.data.codegen.ValueDecoder;
import com.daml.ledger.javaapi.data.codegen.Variant;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfReader;
import java.lang.IllegalArgumentException;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public abstract class AssetType extends Variant<AssetType> {
  public static final String _packageId = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public AssetType() {
  }

  public abstract com.daml.ledger.javaapi.data.Variant toValue();

  public static ValueDecoder<AssetType> valueDecoder() {
    return value$ -> {
      com.daml.ledger.javaapi.data.Variant variant$ = value$.asVariant().orElseThrow(() -> new IllegalArgumentException("Expected Variant to build an instance of the Variant com.canton.etf.model.canton.etf.primebrokerage.collateralasset.AssetType"));
      if ("TokenizedTreasury".equals(variant$.getConstructor())) {
        return valueDecoderTokenizedTreasury().decode(variant$);
      }
      if ("BitcoinCollateral".equals(variant$.getConstructor())) {
        return valueDecoderBitcoinCollateral().decode(variant$);
      }
      if ("MoneyMarketFundShare".equals(variant$.getConstructor())) {
        return valueDecoderMoneyMarketFundShare().decode(variant$);
      }
      throw new IllegalArgumentException("Found unknown constructor " + variant$.getConstructor() + " for variant com.canton.etf.model.canton.etf.primebrokerage.collateralasset.AssetType, expected one of [TokenizedTreasury, BitcoinCollateral, MoneyMarketFundShare]. This could be a failed variant downgrade.");
    } ;
  }

  public static JsonLfDecoder<AssetType> jsonDecoder() {
    return JsonLfDecoders.variant(Arrays.asList("TokenizedTreasury", "BitcoinCollateral", "MoneyMarketFundShare"), name -> {
          switch (name) {
            case "TokenizedTreasury": return jsonDecoderTokenizedTreasury();
            case "BitcoinCollateral": return jsonDecoderBitcoinCollateral();
            case "MoneyMarketFundShare": return jsonDecoderMoneyMarketFundShare();
            default: return null;
          }
        }
        );
  }

  public static AssetType fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  protected abstract JsonLfEncoders.Field fieldForJsonEncoder();

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.variant(AssetType::fieldForJsonEncoder).apply(this);
  }

  public static ValueDecoder<TokenizedTreasury> valueDecoderTokenizedTreasury() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = PrimitiveValueDecoders.variantCheck("TokenizedTreasury", value$);
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(5,0, recordValue$);
      String cusip = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      LocalDate maturityDate = PrimitiveValueDecoders.fromDate.decode(fields$.get(1).getValue());
      BigDecimal faceValue = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(2).getValue());
      String issuer = PrimitiveValueDecoders.fromText.decode(fields$.get(3).getValue());
      BigDecimal accruedInterest = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(4).getValue());
      return new TokenizedTreasury(cusip, maturityDate, faceValue, issuer, accruedInterest);
    } ;
  }

  private static JsonLfDecoder<TokenizedTreasury> jsonDecoderTokenizedTreasury() {
    return JsonLfDecoders.record(Arrays.asList("cusip", "maturityDate", "faceValue", "issuer", "accruedInterest"), name -> {
          switch (name) {
            case "cusip": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "maturityDate": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.date);
            case "faceValue": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "issuer": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "accruedInterest": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new TokenizedTreasury(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4])));
  }

  public static ValueDecoder<MoneyMarketFundShare> valueDecoderMoneyMarketFundShare() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = PrimitiveValueDecoders.variantCheck("MoneyMarketFundShare", value$);
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(4,0, recordValue$);
      String fundName = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      String shareClass = PrimitiveValueDecoders.fromText.decode(fields$.get(1).getValue());
      BigDecimal navPerShare = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(2).getValue());
      BigDecimal quantity = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(3).getValue());
      return new MoneyMarketFundShare(fundName, shareClass, navPerShare, quantity);
    } ;
  }

  private static JsonLfDecoder<MoneyMarketFundShare> jsonDecoderMoneyMarketFundShare() {
    return JsonLfDecoders.record(Arrays.asList("fundName", "shareClass", "navPerShare", "quantity"), name -> {
          switch (name) {
            case "fundName": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "shareClass": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "navPerShare": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "quantity": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new MoneyMarketFundShare(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3])));
  }

  public static ValueDecoder<BitcoinCollateral> valueDecoderBitcoinCollateral() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = PrimitiveValueDecoders.variantCheck("BitcoinCollateral", value$);
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(5,0, recordValue$);
      String custodianRef = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      String walletAddress = PrimitiveValueDecoders.fromText.decode(fields$.get(1).getValue());
      BigDecimal quantity = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(2).getValue());
      BigDecimal spotPrice = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(3).getValue());
      BigDecimal volatility30d = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(4).getValue());
      return new BitcoinCollateral(custodianRef, walletAddress, quantity, spotPrice, volatility30d);
    } ;
  }

  private static JsonLfDecoder<BitcoinCollateral> jsonDecoderBitcoinCollateral() {
    return JsonLfDecoders.record(Arrays.asList("custodianRef", "walletAddress", "quantity", "spotPrice", "volatility30d"), name -> {
          switch (name) {
            case "custodianRef": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "walletAddress": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "quantity": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "spotPrice": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "volatility30d": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new BitcoinCollateral(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4])));
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<AssetType> get() {
      return jsonDecoder();
    }
  }
}
