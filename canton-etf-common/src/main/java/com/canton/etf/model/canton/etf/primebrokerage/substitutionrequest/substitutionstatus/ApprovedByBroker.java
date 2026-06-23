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

public class ApprovedByBroker extends SubstitutionStatus {
  public static final String _packageId = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public final Unit unitValue;

  public ApprovedByBroker(Unit unitValue) {
    this.unitValue = unitValue;
  }

  public Variant toValue() {
    return new Variant("ApprovedByBroker", Unit.getInstance());
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof ApprovedByBroker)) {
      return false;
    }
    ApprovedByBroker other = (ApprovedByBroker) object;
    return Objects.equals(this.unitValue, other.unitValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.unitValue);
  }

  @Override
  public String toString() {
    return String.format("ApprovedByBroker(%s)", this.unitValue);
  }

  @Override
  protected JsonLfEncoders.Field fieldForJsonEncoder() {
    return JsonLfEncoders.Field.of("ApprovedByBroker", apply(JsonLfEncoders::unit, unitValue));
  }
}
