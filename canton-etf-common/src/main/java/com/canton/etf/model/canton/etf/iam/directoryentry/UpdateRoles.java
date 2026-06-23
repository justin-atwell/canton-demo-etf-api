package com.canton.etf.model.canton.etf.iam.directoryentry;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.daml.ledger.javaapi.data.DamlCollectors;
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

public class UpdateRoles extends DamlRecord<UpdateRoles> {
  public static final String _packageId = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public final List<String> newRoles;

  public UpdateRoles(List<String> newRoles) {
    this.newRoles = newRoles;
  }

  public static ValueDecoder<UpdateRoles> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(1,0,
          recordValue$);
      List<String> newRoles = PrimitiveValueDecoders.fromList(PrimitiveValueDecoders.fromText)
          .decode(fields$.get(0).getValue());
      return new UpdateRoles(newRoles);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(1);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("newRoles", this.newRoles.stream().collect(DamlCollectors.toDamlList(v$0 -> new Text(v$0)))));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<UpdateRoles> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("newRoles"), name -> {
          switch (name) {
            case "newRoles": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.list(com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text));
            default: return null;
          }
        }
        , (Object[] args) -> new UpdateRoles(JsonLfDecoders.cast(args[0])));
  }

  public static UpdateRoles fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("newRoles", apply(JsonLfEncoders.list(JsonLfEncoders::text), newRoles)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof UpdateRoles)) {
      return false;
    }
    UpdateRoles other = (UpdateRoles) object;
    return Objects.equals(this.newRoles, other.newRoles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.newRoles);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.iam.directoryentry.UpdateRoles(%s)",
        this.newRoles);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<UpdateRoles> get() {
      return jsonDecoder();
    }
  }
}
