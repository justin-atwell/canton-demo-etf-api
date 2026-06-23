package com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionStatus;
import com.daml.ledger.javaapi.data.Unit;
import com.daml.ledger.javaapi.data.Variant;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Objects;

public class Pending extends SubstitutionStatus {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final Unit unitValue;

  public Pending(Unit unitValue) {
    this.unitValue = unitValue;
  }

  public Variant toValue() {
    return new Variant("Pending", Unit.getInstance());
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof Pending)) {
      return false;
    }
    Pending other = (Pending) object;
    return Objects.equals(this.unitValue, other.unitValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.unitValue);
  }

  @Override
  public String toString() {
    return String.format("Pending(%s)", this.unitValue);
  }

  @Override
  protected JsonLfEncoders.Field fieldForJsonEncoder() {
    return JsonLfEncoders.Field.of("Pending", apply(JsonLfEncoders::unit, unitValue));
  }
}
