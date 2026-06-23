package com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.daml.ledger.javaapi.data.Text;
import com.daml.ledger.javaapi.data.Value;
import com.daml.ledger.javaapi.data.codegen.DamlRecord;
import com.daml.ledger.javaapi.data.codegen.PrimitiveValueDecoders;
import com.daml.ledger.javaapi.data.codegen.ValueDecoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfReader;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RejectByCustodian extends DamlRecord<RejectByCustodian> {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final String reason;

  public RejectByCustodian(String reason) {
    this.reason = reason;
  }

  public static ValueDecoder<RejectByCustodian> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(1,0,
          recordValue$);
      String reason = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      return new RejectByCustodian(reason);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(1);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("reason", new Text(this.reason)));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<RejectByCustodian> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("reason"), name -> {
          switch (name) {
            case "reason": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            default: return null;
          }
        }
        , (Object[] args) -> new RejectByCustodian(JsonLfDecoders.cast(args[0])));
  }

  public static RejectByCustodian fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("reason", apply(JsonLfEncoders::text, reason)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof RejectByCustodian)) {
      return false;
    }
    RejectByCustodian other = (RejectByCustodian) object;
    return Objects.equals(this.reason, other.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.reason);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.RejectByCustodian(%s)",
        this.reason);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<RejectByCustodian> get() {
      return jsonDecoder();
    }
  }
}
