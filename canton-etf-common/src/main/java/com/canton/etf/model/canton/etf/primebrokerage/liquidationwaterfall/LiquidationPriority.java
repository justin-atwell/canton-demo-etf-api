package com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.daml.ledger.javaapi.data.Int64;
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
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LiquidationPriority extends DamlRecord<LiquidationPriority> {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final String assetClass;

  public final Long priority;

  public LiquidationPriority(String assetClass, Long priority) {
    this.assetClass = assetClass;
    this.priority = priority;
  }

  public static ValueDecoder<LiquidationPriority> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(2,0,
          recordValue$);
      String assetClass = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      Long priority = PrimitiveValueDecoders.fromInt64.decode(fields$.get(1).getValue());
      return new LiquidationPriority(assetClass, priority);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(2);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("assetClass", new Text(this.assetClass)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("priority", new Int64(this.priority)));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<LiquidationPriority> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("assetClass", "priority"), name -> {
          switch (name) {
            case "assetClass": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "priority": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.int64);
            default: return null;
          }
        }
        , (Object[] args) -> new LiquidationPriority(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1])));
  }

  public static LiquidationPriority fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("assetClass", apply(JsonLfEncoders::text, assetClass)),
        JsonLfEncoders.Field.of("priority", apply(JsonLfEncoders::int64, priority)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof LiquidationPriority)) {
      return false;
    }
    LiquidationPriority other = (LiquidationPriority) object;
    return Objects.equals(this.assetClass, other.assetClass) &&
        Objects.equals(this.priority, other.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.assetClass, this.priority);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationPriority(%s, %s)",
        this.assetClass, this.priority);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<LiquidationPriority> get() {
      return jsonDecoder();
    }
  }
}
