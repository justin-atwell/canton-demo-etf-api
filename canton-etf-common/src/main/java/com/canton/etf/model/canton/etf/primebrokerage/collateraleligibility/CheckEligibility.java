package com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition;
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

public class CheckEligibility extends DamlRecord<CheckEligibility> {
  public static final String _packageId = "c2e41b9e13dba8a6699eb8b7cde2c5e5c96ac00b38b18aa67373f159da67c93d";

  public final CollateralPosition position;

  public CheckEligibility(CollateralPosition position) {
    this.position = position;
  }

  public static ValueDecoder<CheckEligibility> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(1,0,
          recordValue$);
      CollateralPosition position = CollateralPosition.valueDecoder()
          .decode(fields$.get(0).getValue());
      return new CheckEligibility(position);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(1);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("position", this.position.toValue()));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<CheckEligibility> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("position"), name -> {
          switch (name) {
            case "position": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, new com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition.JsonDecoder$().get());
            default: return null;
          }
        }
        , (Object[] args) -> new CheckEligibility(JsonLfDecoders.cast(args[0])));
  }

  public static CheckEligibility fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("position", apply(CollateralPosition::jsonEncoder, position)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof CheckEligibility)) {
      return false;
    }
    CheckEligibility other = (CheckEligibility) object;
    return Objects.equals(this.position, other.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.position);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.CheckEligibility(%s)",
        this.position);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<CheckEligibility> get() {
      return jsonDecoder();
    }
  }
}
