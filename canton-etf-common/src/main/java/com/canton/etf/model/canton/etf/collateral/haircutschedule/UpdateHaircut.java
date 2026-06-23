package com.canton.etf.model.canton.etf.collateral.haircutschedule;

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

public class UpdateHaircut extends DamlRecord<UpdateHaircut> {
  public static final String _packageId = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public final String asset;

  public final BigDecimal rate;

  public UpdateHaircut(String asset, BigDecimal rate) {
    this.asset = asset;
    this.rate = rate;
  }

  public static ValueDecoder<UpdateHaircut> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(2,0,
          recordValue$);
      String asset = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      BigDecimal rate = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(1).getValue());
      return new UpdateHaircut(asset, rate);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(2);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("asset", new Text(this.asset)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("rate", new Numeric(this.rate)));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<UpdateHaircut> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("asset", "rate"), name -> {
          switch (name) {
            case "asset": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "rate": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new UpdateHaircut(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1])));
  }

  public static UpdateHaircut fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("asset", apply(JsonLfEncoders::text, asset)),
        JsonLfEncoders.Field.of("rate", apply(JsonLfEncoders::numeric, rate)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof UpdateHaircut)) {
      return false;
    }
    UpdateHaircut other = (UpdateHaircut) object;
    return Objects.equals(this.asset, other.asset) && Objects.equals(this.rate, other.rate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.asset, this.rate);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.collateral.haircutschedule.UpdateHaircut(%s, %s)",
        this.asset, this.rate);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<UpdateHaircut> get() {
      return jsonDecoder();
    }
  }
}
