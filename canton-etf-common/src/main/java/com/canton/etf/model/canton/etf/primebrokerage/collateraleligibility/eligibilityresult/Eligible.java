package com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.eligibilityresult;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.EligibilityResult;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.Numeric;
import com.daml.ledger.javaapi.data.Variant;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

public class Eligible extends EligibilityResult {
  public static final String _packageId = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public final BigDecimal haircutAdjustedValue;

  public Eligible(BigDecimal haircutAdjustedValue) {
    this.haircutAdjustedValue = haircutAdjustedValue;
  }

  public Variant toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(1);
    fields.add(new DamlRecord.Field("haircutAdjustedValue", new Numeric(this.haircutAdjustedValue)));
    return new Variant("Eligible", new DamlRecord(fields));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof Eligible)) {
      return false;
    }
    Eligible other = (Eligible) object;
    return Objects.equals(this.haircutAdjustedValue, other.haircutAdjustedValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.haircutAdjustedValue);
  }

  @Override
  public String toString() {
    return String.format("Eligible(%s)", this.haircutAdjustedValue);
  }

  private JsonLfEncoder jsonEncoderEligible() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("haircutAdjustedValue", apply(JsonLfEncoders::numeric, haircutAdjustedValue)));
  }

  @Override
  protected JsonLfEncoders.Field fieldForJsonEncoder() {
    return JsonLfEncoders.Field.of("Eligible", this.jsonEncoderEligible());
  }
}
