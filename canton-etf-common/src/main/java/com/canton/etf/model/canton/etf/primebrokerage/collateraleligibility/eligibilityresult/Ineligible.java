package com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.eligibilityresult;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.EligibilityResult;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.Text;
import com.daml.ledger.javaapi.data.Variant;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.Objects;

public class Ineligible extends EligibilityResult {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final String reason;

  public Ineligible(String reason) {
    this.reason = reason;
  }

  public Variant toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(1);
    fields.add(new DamlRecord.Field("reason", new Text(this.reason)));
    return new Variant("Ineligible", new DamlRecord(fields));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof Ineligible)) {
      return false;
    }
    Ineligible other = (Ineligible) object;
    return Objects.equals(this.reason, other.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.reason);
  }

  @Override
  public String toString() {
    return String.format("Ineligible(%s)", this.reason);
  }

  private JsonLfEncoder jsonEncoderIneligible() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("reason", apply(JsonLfEncoders::text, reason)));
  }

  @Override
  protected JsonLfEncoders.Field fieldForJsonEncoder() {
    return JsonLfEncoders.Field.of("Ineligible", this.jsonEncoderIneligible());
  }
}
