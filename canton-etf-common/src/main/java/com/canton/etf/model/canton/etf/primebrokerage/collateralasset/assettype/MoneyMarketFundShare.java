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

public class MoneyMarketFundShare extends AssetType {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final String fundName;

  public final String shareClass;

  public final BigDecimal navPerShare;

  public final BigDecimal quantity;

  public MoneyMarketFundShare(String fundName, String shareClass, BigDecimal navPerShare,
      BigDecimal quantity) {
    this.fundName = fundName;
    this.shareClass = shareClass;
    this.navPerShare = navPerShare;
    this.quantity = quantity;
  }

  public Variant toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(4);
    fields.add(new DamlRecord.Field("fundName", new Text(this.fundName)));
    fields.add(new DamlRecord.Field("shareClass", new Text(this.shareClass)));
    fields.add(new DamlRecord.Field("navPerShare", new Numeric(this.navPerShare)));
    fields.add(new DamlRecord.Field("quantity", new Numeric(this.quantity)));
    return new Variant("MoneyMarketFundShare", new DamlRecord(fields));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof MoneyMarketFundShare)) {
      return false;
    }
    MoneyMarketFundShare other = (MoneyMarketFundShare) object;
    return Objects.equals(this.fundName, other.fundName) &&
        Objects.equals(this.shareClass, other.shareClass) &&
        Objects.equals(this.navPerShare, other.navPerShare) &&
        Objects.equals(this.quantity, other.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fundName, this.shareClass, this.navPerShare, this.quantity);
  }

  @Override
  public String toString() {
    return String.format("MoneyMarketFundShare(%s, %s, %s, %s)", this.fundName, this.shareClass,
        this.navPerShare, this.quantity);
  }

  private JsonLfEncoder jsonEncoderMoneyMarketFundShare() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("fundName", apply(JsonLfEncoders::text, fundName)),
        JsonLfEncoders.Field.of("shareClass", apply(JsonLfEncoders::text, shareClass)),
        JsonLfEncoders.Field.of("navPerShare", apply(JsonLfEncoders::numeric, navPerShare)),
        JsonLfEncoders.Field.of("quantity", apply(JsonLfEncoders::numeric, quantity)));
  }

  @Override
  protected JsonLfEncoders.Field fieldForJsonEncoder() {
    return JsonLfEncoders.Field.of("MoneyMarketFundShare", this.jsonEncoderMoneyMarketFundShare());
  }
}
