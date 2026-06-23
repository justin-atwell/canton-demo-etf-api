package com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionStatus;
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

public class Rejected extends SubstitutionStatus {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final String rejectionReason;

  public Rejected(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  public Variant toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(1);
    fields.add(new DamlRecord.Field("rejectionReason", new Text(this.rejectionReason)));
    return new Variant("Rejected", new DamlRecord(fields));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof Rejected)) {
      return false;
    }
    Rejected other = (Rejected) object;
    return Objects.equals(this.rejectionReason, other.rejectionReason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.rejectionReason);
  }

  @Override
  public String toString() {
    return String.format("Rejected(%s)", this.rejectionReason);
  }

  private JsonLfEncoder jsonEncoderRejected() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("rejectionReason", apply(JsonLfEncoders::text, rejectionReason)));
  }

  @Override
  protected JsonLfEncoders.Field fieldForJsonEncoder() {
    return JsonLfEncoders.Field.of("Rejected", this.jsonEncoderRejected());
  }
}
