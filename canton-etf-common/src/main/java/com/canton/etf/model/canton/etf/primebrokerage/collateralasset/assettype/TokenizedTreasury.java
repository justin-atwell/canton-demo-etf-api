package com.canton.etf.model.canton.etf.primebrokerage.collateralasset.assettype;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.AssetType;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.Date;
import com.daml.ledger.javaapi.data.Numeric;
import com.daml.ledger.javaapi.data.Text;
import com.daml.ledger.javaapi.data.Variant;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class TokenizedTreasury extends AssetType {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final String cusip;

  public final LocalDate maturityDate;

  public final BigDecimal faceValue;

  public final String issuer;

  public final BigDecimal accruedInterest;

  public TokenizedTreasury(String cusip, LocalDate maturityDate, BigDecimal faceValue,
      String issuer, BigDecimal accruedInterest) {
    this.cusip = cusip;
    this.maturityDate = maturityDate;
    this.faceValue = faceValue;
    this.issuer = issuer;
    this.accruedInterest = accruedInterest;
  }

  public Variant toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(5);
    fields.add(new DamlRecord.Field("cusip", new Text(this.cusip)));
    fields.add(new DamlRecord.Field("maturityDate", new Date((int) this.maturityDate.toEpochDay())));
    fields.add(new DamlRecord.Field("faceValue", new Numeric(this.faceValue)));
    fields.add(new DamlRecord.Field("issuer", new Text(this.issuer)));
    fields.add(new DamlRecord.Field("accruedInterest", new Numeric(this.accruedInterest)));
    return new Variant("TokenizedTreasury", new DamlRecord(fields));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof TokenizedTreasury)) {
      return false;
    }
    TokenizedTreasury other = (TokenizedTreasury) object;
    return Objects.equals(this.cusip, other.cusip) &&
        Objects.equals(this.maturityDate, other.maturityDate) &&
        Objects.equals(this.faceValue, other.faceValue) &&
        Objects.equals(this.issuer, other.issuer) &&
        Objects.equals(this.accruedInterest, other.accruedInterest);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.cusip, this.maturityDate, this.faceValue, this.issuer,
        this.accruedInterest);
  }

  @Override
  public String toString() {
    return String.format("TokenizedTreasury(%s, %s, %s, %s, %s)", this.cusip, this.maturityDate,
        this.faceValue, this.issuer, this.accruedInterest);
  }

  private JsonLfEncoder jsonEncoderTokenizedTreasury() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("cusip", apply(JsonLfEncoders::text, cusip)),
        JsonLfEncoders.Field.of("maturityDate", apply(JsonLfEncoders::date, maturityDate)),
        JsonLfEncoders.Field.of("faceValue", apply(JsonLfEncoders::numeric, faceValue)),
        JsonLfEncoders.Field.of("issuer", apply(JsonLfEncoders::text, issuer)),
        JsonLfEncoders.Field.of("accruedInterest", apply(JsonLfEncoders::numeric, accruedInterest)));
  }

  @Override
  protected JsonLfEncoders.Field fieldForJsonEncoder() {
    return JsonLfEncoders.Field.of("TokenizedTreasury", this.jsonEncoderTokenizedTreasury());
  }
}
