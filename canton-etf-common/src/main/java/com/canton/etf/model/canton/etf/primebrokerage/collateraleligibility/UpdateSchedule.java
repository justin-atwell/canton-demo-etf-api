package com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.daml.ledger.javaapi.data.Numeric;
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

public class UpdateSchedule extends DamlRecord<UpdateSchedule> {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final BigDecimal newMaxBtcConc;

  public final BigDecimal newMaxTreasuryConc;

  public final BigDecimal newMaxMmfConc;

  public final BigDecimal newMinValue;

  public UpdateSchedule(BigDecimal newMaxBtcConc, BigDecimal newMaxTreasuryConc,
      BigDecimal newMaxMmfConc, BigDecimal newMinValue) {
    this.newMaxBtcConc = newMaxBtcConc;
    this.newMaxTreasuryConc = newMaxTreasuryConc;
    this.newMaxMmfConc = newMaxMmfConc;
    this.newMinValue = newMinValue;
  }

  public static ValueDecoder<UpdateSchedule> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(4,0,
          recordValue$);
      BigDecimal newMaxBtcConc = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(0).getValue());
      BigDecimal newMaxTreasuryConc = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(1).getValue());
      BigDecimal newMaxMmfConc = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(2).getValue());
      BigDecimal newMinValue = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(3).getValue());
      return new UpdateSchedule(newMaxBtcConc, newMaxTreasuryConc, newMaxMmfConc, newMinValue);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(4);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("newMaxBtcConc", new Numeric(this.newMaxBtcConc)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("newMaxTreasuryConc", new Numeric(this.newMaxTreasuryConc)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("newMaxMmfConc", new Numeric(this.newMaxMmfConc)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("newMinValue", new Numeric(this.newMinValue)));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<UpdateSchedule> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("newMaxBtcConc", "newMaxTreasuryConc", "newMaxMmfConc", "newMinValue"), name -> {
          switch (name) {
            case "newMaxBtcConc": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "newMaxTreasuryConc": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "newMaxMmfConc": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "newMinValue": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new UpdateSchedule(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3])));
  }

  public static UpdateSchedule fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("newMaxBtcConc", apply(JsonLfEncoders::numeric, newMaxBtcConc)),
        JsonLfEncoders.Field.of("newMaxTreasuryConc", apply(JsonLfEncoders::numeric, newMaxTreasuryConc)),
        JsonLfEncoders.Field.of("newMaxMmfConc", apply(JsonLfEncoders::numeric, newMaxMmfConc)),
        JsonLfEncoders.Field.of("newMinValue", apply(JsonLfEncoders::numeric, newMinValue)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof UpdateSchedule)) {
      return false;
    }
    UpdateSchedule other = (UpdateSchedule) object;
    return Objects.equals(this.newMaxBtcConc, other.newMaxBtcConc) &&
        Objects.equals(this.newMaxTreasuryConc, other.newMaxTreasuryConc) &&
        Objects.equals(this.newMaxMmfConc, other.newMaxMmfConc) &&
        Objects.equals(this.newMinValue, other.newMinValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.newMaxBtcConc, this.newMaxTreasuryConc, this.newMaxMmfConc,
        this.newMinValue);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.UpdateSchedule(%s, %s, %s, %s)",
        this.newMaxBtcConc, this.newMaxTreasuryConc, this.newMaxMmfConc, this.newMinValue);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<UpdateSchedule> get() {
      return jsonDecoder();
    }
  }
}
