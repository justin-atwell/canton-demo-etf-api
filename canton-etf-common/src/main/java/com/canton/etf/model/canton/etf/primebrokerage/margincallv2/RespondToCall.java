package com.canton.etf.model.canton.etf.primebrokerage.margincallv2;

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

public class RespondToCall extends DamlRecord<RespondToCall> {
  public static final String _packageId = "c2e41b9e13dba8a6699eb8b7cde2c5e5c96ac00b38b18aa67373f159da67c93d";

  public final ResponseType responseType;

  public final String comment;

  public RespondToCall(ResponseType responseType, String comment) {
    this.responseType = responseType;
    this.comment = comment;
  }

  public static ValueDecoder<RespondToCall> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(2,0,
          recordValue$);
      ResponseType responseType = ResponseType.valueDecoder().decode(fields$.get(0).getValue());
      String comment = PrimitiveValueDecoders.fromText.decode(fields$.get(1).getValue());
      return new RespondToCall(responseType, comment);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(2);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("responseType", this.responseType.toValue()));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("comment", new Text(this.comment)));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<RespondToCall> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("responseType", "comment"), name -> {
          switch (name) {
            case "responseType": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, new com.canton.etf.model.canton.etf.primebrokerage.margincallv2.ResponseType.JsonDecoder$().get());
            case "comment": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            default: return null;
          }
        }
        , (Object[] args) -> new RespondToCall(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1])));
  }

  public static RespondToCall fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("responseType", apply(ResponseType::jsonEncoder, responseType)),
        JsonLfEncoders.Field.of("comment", apply(JsonLfEncoders::text, comment)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof RespondToCall)) {
      return false;
    }
    RespondToCall other = (RespondToCall) object;
    return Objects.equals(this.responseType, other.responseType) &&
        Objects.equals(this.comment, other.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.responseType, this.comment);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.margincallv2.RespondToCall(%s, %s)",
        this.responseType, this.comment);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<RespondToCall> get() {
      return jsonDecoder();
    }
  }
}
