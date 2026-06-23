package com.canton.etf.model.canton.etf.primebrokerage.collateralpool;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition;
import com.daml.ledger.javaapi.data.DamlCollectors;
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

public class RevaluePool extends DamlRecord<RevaluePool> {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final List<CollateralPosition> updatedPositions;

  public RevaluePool(List<CollateralPosition> updatedPositions) {
    this.updatedPositions = updatedPositions;
  }

  public static ValueDecoder<RevaluePool> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(1,0,
          recordValue$);
      List<CollateralPosition> updatedPositions = PrimitiveValueDecoders.fromList(
            CollateralPosition.valueDecoder()).decode(fields$.get(0).getValue());
      return new RevaluePool(updatedPositions);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(1);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("updatedPositions", this.updatedPositions.stream().collect(DamlCollectors.toDamlList(v$0 -> v$0.toValue()))));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<RevaluePool> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("updatedPositions"), name -> {
          switch (name) {
            case "updatedPositions": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.list(new com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition.JsonDecoder$().get()));
            default: return null;
          }
        }
        , (Object[] args) -> new RevaluePool(JsonLfDecoders.cast(args[0])));
  }

  public static RevaluePool fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("updatedPositions", apply(JsonLfEncoders.list(CollateralPosition::jsonEncoder), updatedPositions)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof RevaluePool)) {
      return false;
    }
    RevaluePool other = (RevaluePool) object;
    return Objects.equals(this.updatedPositions, other.updatedPositions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.updatedPositions);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.collateralpool.RevaluePool(%s)",
        this.updatedPositions);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<RevaluePool> get() {
      return jsonDecoder();
    }
  }
}
