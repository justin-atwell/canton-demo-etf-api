package com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

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

public class ExecuteWaterfall extends DamlRecord<ExecuteWaterfall> {
  public static final String _packageId = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public final List<LiquidationPriority> priorities;

  public ExecuteWaterfall(List<LiquidationPriority> priorities) {
    this.priorities = priorities;
  }

  public static ValueDecoder<ExecuteWaterfall> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(1,0,
          recordValue$);
      List<LiquidationPriority> priorities = PrimitiveValueDecoders.fromList(
            LiquidationPriority.valueDecoder()).decode(fields$.get(0).getValue());
      return new ExecuteWaterfall(priorities);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(1);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("priorities", this.priorities.stream().collect(DamlCollectors.toDamlList(v$0 -> v$0.toValue()))));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<ExecuteWaterfall> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("priorities"), name -> {
          switch (name) {
            case "priorities": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.list(new com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationPriority.JsonDecoder$().get()));
            default: return null;
          }
        }
        , (Object[] args) -> new ExecuteWaterfall(JsonLfDecoders.cast(args[0])));
  }

  public static ExecuteWaterfall fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("priorities", apply(JsonLfEncoders.list(LiquidationPriority::jsonEncoder), priorities)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof ExecuteWaterfall)) {
      return false;
    }
    ExecuteWaterfall other = (ExecuteWaterfall) object;
    return Objects.equals(this.priorities, other.priorities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.priorities);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.ExecuteWaterfall(%s)",
        this.priorities);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<ExecuteWaterfall> get() {
      return jsonDecoder();
    }
  }
}
