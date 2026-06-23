package com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.daml.ledger.javaapi.data.Numeric;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LiquidationStep extends DamlRecord<LiquidationStep> {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final String positionId;

  public final String assetClass;

  public final BigDecimal grossValue;

  public final BigDecimal haircutAdj;

  public final BigDecimal appliedToShortfall;

  public final BigDecimal remainingShortfall;

  public LiquidationStep(String positionId, String assetClass, BigDecimal grossValue,
      BigDecimal haircutAdj, BigDecimal appliedToShortfall, BigDecimal remainingShortfall) {
    this.positionId = positionId;
    this.assetClass = assetClass;
    this.grossValue = grossValue;
    this.haircutAdj = haircutAdj;
    this.appliedToShortfall = appliedToShortfall;
    this.remainingShortfall = remainingShortfall;
  }

  public static ValueDecoder<LiquidationStep> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(6,0,
          recordValue$);
      String positionId = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      String assetClass = PrimitiveValueDecoders.fromText.decode(fields$.get(1).getValue());
      BigDecimal grossValue = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(2).getValue());
      BigDecimal haircutAdj = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(3).getValue());
      BigDecimal appliedToShortfall = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(4).getValue());
      BigDecimal remainingShortfall = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(5).getValue());
      return new LiquidationStep(positionId, assetClass, grossValue, haircutAdj, appliedToShortfall,
          remainingShortfall);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(6);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("positionId", new Text(this.positionId)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("assetClass", new Text(this.assetClass)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("grossValue", new Numeric(this.grossValue)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("haircutAdj", new Numeric(this.haircutAdj)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("appliedToShortfall", new Numeric(this.appliedToShortfall)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("remainingShortfall", new Numeric(this.remainingShortfall)));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<LiquidationStep> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("positionId", "assetClass", "grossValue", "haircutAdj", "appliedToShortfall", "remainingShortfall"), name -> {
          switch (name) {
            case "positionId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "assetClass": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "grossValue": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "haircutAdj": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "appliedToShortfall": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "remainingShortfall": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new LiquidationStep(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5])));
  }

  public static LiquidationStep fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("positionId", apply(JsonLfEncoders::text, positionId)),
        JsonLfEncoders.Field.of("assetClass", apply(JsonLfEncoders::text, assetClass)),
        JsonLfEncoders.Field.of("grossValue", apply(JsonLfEncoders::numeric, grossValue)),
        JsonLfEncoders.Field.of("haircutAdj", apply(JsonLfEncoders::numeric, haircutAdj)),
        JsonLfEncoders.Field.of("appliedToShortfall", apply(JsonLfEncoders::numeric, appliedToShortfall)),
        JsonLfEncoders.Field.of("remainingShortfall", apply(JsonLfEncoders::numeric, remainingShortfall)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof LiquidationStep)) {
      return false;
    }
    LiquidationStep other = (LiquidationStep) object;
    return Objects.equals(this.positionId, other.positionId) &&
        Objects.equals(this.assetClass, other.assetClass) &&
        Objects.equals(this.grossValue, other.grossValue) &&
        Objects.equals(this.haircutAdj, other.haircutAdj) &&
        Objects.equals(this.appliedToShortfall, other.appliedToShortfall) &&
        Objects.equals(this.remainingShortfall, other.remainingShortfall);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.positionId, this.assetClass, this.grossValue, this.haircutAdj,
        this.appliedToShortfall, this.remainingShortfall);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationStep(%s, %s, %s, %s, %s, %s)",
        this.positionId, this.assetClass, this.grossValue, this.haircutAdj, this.appliedToShortfall,
        this.remainingShortfall);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<LiquidationStep> get() {
      return jsonDecoder();
    }
  }
}
