package com.canton.etf.model.canton.etf.fund.etfdefinition;

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

public class AddConstituent extends DamlRecord<AddConstituent> {
  public static final String _packageId = "1f28d7b7af3161c75a78b37a77e89a8c08d9579c841613879009c4365fd6566c";

  public final String symbol;

  public final String name;

  public final String cusip;

  public final BigDecimal weight;

  public AddConstituent(String symbol, String name, String cusip, BigDecimal weight) {
    this.symbol = symbol;
    this.name = name;
    this.cusip = cusip;
    this.weight = weight;
  }

  public static ValueDecoder<AddConstituent> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(4,0,
          recordValue$);
      String symbol = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      String name = PrimitiveValueDecoders.fromText.decode(fields$.get(1).getValue());
      String cusip = PrimitiveValueDecoders.fromText.decode(fields$.get(2).getValue());
      BigDecimal weight = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(3).getValue());
      return new AddConstituent(symbol, name, cusip, weight);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(4);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("symbol", new Text(this.symbol)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("name", new Text(this.name)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("cusip", new Text(this.cusip)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("weight", new Numeric(this.weight)));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<AddConstituent> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("symbol", "name", "cusip", "weight"), name -> {
          switch (name) {
            case "symbol": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "name": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "cusip": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "weight": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new AddConstituent(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3])));
  }

  public static AddConstituent fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("symbol", apply(JsonLfEncoders::text, symbol)),
        JsonLfEncoders.Field.of("name", apply(JsonLfEncoders::text, name)),
        JsonLfEncoders.Field.of("cusip", apply(JsonLfEncoders::text, cusip)),
        JsonLfEncoders.Field.of("weight", apply(JsonLfEncoders::numeric, weight)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof AddConstituent)) {
      return false;
    }
    AddConstituent other = (AddConstituent) object;
    return Objects.equals(this.symbol, other.symbol) && Objects.equals(this.name, other.name) &&
        Objects.equals(this.cusip, other.cusip) && Objects.equals(this.weight, other.weight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.symbol, this.name, this.cusip, this.weight);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.fund.etfdefinition.AddConstituent(%s, %s, %s, %s)",
        this.symbol, this.name, this.cusip, this.weight);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<AddConstituent> get() {
      return jsonDecoder();
    }
  }
}
