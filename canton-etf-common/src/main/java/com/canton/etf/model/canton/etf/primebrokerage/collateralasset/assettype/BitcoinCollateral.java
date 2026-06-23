package com.canton.etf.model.canton.etf.primebrokerage.collateralasset.assettype;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.AssetType;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.Numeric;
import com.daml.ledger.javaapi.data.Text;
import com.daml.ledger.javaapi.data.Variant;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class BitcoinCollateral extends AssetType {
  public static final String _packageId = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public final String custodianRef;

  public final String walletAddress;

  public final BigDecimal quantity;

  public final BigDecimal spotPrice;

  public final BigDecimal volatility30d;

  public BitcoinCollateral(String custodianRef, String walletAddress, BigDecimal quantity,
      BigDecimal spotPrice, BigDecimal volatility30d) {
    this.custodianRef = custodianRef;
    this.walletAddress = walletAddress;
    this.quantity = quantity;
    this.spotPrice = spotPrice;
    this.volatility30d = volatility30d;
  }

  public Variant toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(5);
    fields.add(new DamlRecord.Field("custodianRef", new Text(this.custodianRef)));
    fields.add(new DamlRecord.Field("walletAddress", new Text(this.walletAddress)));
    fields.add(new DamlRecord.Field("quantity", new Numeric(this.quantity)));
    fields.add(new DamlRecord.Field("spotPrice", new Numeric(this.spotPrice)));
    fields.add(new DamlRecord.Field("volatility30d", new Numeric(this.volatility30d)));
    return new Variant("BitcoinCollateral", new DamlRecord(fields));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof BitcoinCollateral)) {
      return false;
    }
    BitcoinCollateral other = (BitcoinCollateral) object;
    return Objects.equals(this.custodianRef, other.custodianRef) &&
        Objects.equals(this.walletAddress, other.walletAddress) &&
        Objects.equals(this.quantity, other.quantity) &&
        Objects.equals(this.spotPrice, other.spotPrice) &&
        Objects.equals(this.volatility30d, other.volatility30d);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.custodianRef, this.walletAddress, this.quantity, this.spotPrice,
        this.volatility30d);
  }

  @Override
  public String toString() {
    return String.format("BitcoinCollateral(%s, %s, %s, %s, %s)", this.custodianRef,
        this.walletAddress, this.quantity, this.spotPrice, this.volatility30d);
  }

  private JsonLfEncoder jsonEncoderBitcoinCollateral() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("custodianRef", apply(JsonLfEncoders::text, custodianRef)),
        JsonLfEncoders.Field.of("walletAddress", apply(JsonLfEncoders::text, walletAddress)),
        JsonLfEncoders.Field.of("quantity", apply(JsonLfEncoders::numeric, quantity)),
        JsonLfEncoders.Field.of("spotPrice", apply(JsonLfEncoders::numeric, spotPrice)),
        JsonLfEncoders.Field.of("volatility30d", apply(JsonLfEncoders::numeric, volatility30d)));
  }

  @Override
  protected JsonLfEncoders.Field fieldForJsonEncoder() {
    return JsonLfEncoders.Field.of("BitcoinCollateral", this.jsonEncoderBitcoinCollateral());
  }
}
