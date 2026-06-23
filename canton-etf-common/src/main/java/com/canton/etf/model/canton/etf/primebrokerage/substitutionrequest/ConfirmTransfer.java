package com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest;

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

public class ConfirmTransfer extends DamlRecord<ConfirmTransfer> {
  public static final String _packageId = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public final CollateralPosition newIncomingPosition;

  public ConfirmTransfer(CollateralPosition newIncomingPosition) {
    this.newIncomingPosition = newIncomingPosition;
  }

  public static ValueDecoder<ConfirmTransfer> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(1,0,
          recordValue$);
      CollateralPosition newIncomingPosition = CollateralPosition.valueDecoder()
          .decode(fields$.get(0).getValue());
      return new ConfirmTransfer(newIncomingPosition);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(1);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("newIncomingPosition", this.newIncomingPosition.toValue()));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<ConfirmTransfer> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("newIncomingPosition"), name -> {
          switch (name) {
            case "newIncomingPosition": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, new com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition.JsonDecoder$().get());
            default: return null;
          }
        }
        , (Object[] args) -> new ConfirmTransfer(JsonLfDecoders.cast(args[0])));
  }

  public static ConfirmTransfer fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("newIncomingPosition", apply(CollateralPosition::jsonEncoder, newIncomingPosition)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof ConfirmTransfer)) {
      return false;
    }
    ConfirmTransfer other = (ConfirmTransfer) object;
    return Objects.equals(this.newIncomingPosition, other.newIncomingPosition);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.newIncomingPosition);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.ConfirmTransfer(%s)",
        this.newIncomingPosition);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<ConfirmTransfer> get() {
      return jsonDecoder();
    }
  }
}
