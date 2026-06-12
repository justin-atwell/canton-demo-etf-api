package com.canton.etf.model.canton.etf.primebrokerage.collateralasset;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.daml.ledger.javaapi.data.Party;
import com.daml.ledger.javaapi.data.Text;
import com.daml.ledger.javaapi.data.Timestamp;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CollateralPosition extends DamlRecord<CollateralPosition> {
  public static final String _packageId = "c2e41b9e13dba8a6699eb8b7cde2c5e5c96ac00b38b18aa67373f159da67c93d";

  public final AssetType asset;

  public final HaircutRate haircut;

  public final String positionId;

  public final String postingParty;

  public final Instant valuedAt;

  public CollateralPosition(AssetType asset, HaircutRate haircut, String positionId,
      String postingParty, Instant valuedAt) {
    this.asset = asset;
    this.haircut = haircut;
    this.positionId = positionId;
    this.postingParty = postingParty;
    this.valuedAt = valuedAt;
  }

  public static ValueDecoder<CollateralPosition> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(5,0,
          recordValue$);
      AssetType asset = AssetType.valueDecoder().decode(fields$.get(0).getValue());
      HaircutRate haircut = HaircutRate.valueDecoder().decode(fields$.get(1).getValue());
      String positionId = PrimitiveValueDecoders.fromText.decode(fields$.get(2).getValue());
      String postingParty = PrimitiveValueDecoders.fromParty.decode(fields$.get(3).getValue());
      Instant valuedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(4).getValue());
      return new CollateralPosition(asset, haircut, positionId, postingParty, valuedAt);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(5);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("asset", this.asset.toValue()));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("haircut", this.haircut.toValue()));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("positionId", new Text(this.positionId)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("postingParty", new Party(this.postingParty)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("valuedAt", Timestamp.fromInstant(this.valuedAt)));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<CollateralPosition> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("asset", "haircut", "positionId", "postingParty", "valuedAt"), name -> {
          switch (name) {
            case "asset": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, new com.canton.etf.model.canton.etf.primebrokerage.collateralasset.AssetType.JsonDecoder$().get());
            case "haircut": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, new com.canton.etf.model.canton.etf.primebrokerage.collateralasset.HaircutRate.JsonDecoder$().get());
            case "positionId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "postingParty": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "valuedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new CollateralPosition(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4])));
  }

  public static CollateralPosition fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("asset", apply(AssetType::jsonEncoder, asset)),
        JsonLfEncoders.Field.of("haircut", apply(HaircutRate::jsonEncoder, haircut)),
        JsonLfEncoders.Field.of("positionId", apply(JsonLfEncoders::text, positionId)),
        JsonLfEncoders.Field.of("postingParty", apply(JsonLfEncoders::party, postingParty)),
        JsonLfEncoders.Field.of("valuedAt", apply(JsonLfEncoders::timestamp, valuedAt)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof CollateralPosition)) {
      return false;
    }
    CollateralPosition other = (CollateralPosition) object;
    return Objects.equals(this.asset, other.asset) && Objects.equals(this.haircut, other.haircut) &&
        Objects.equals(this.positionId, other.positionId) &&
        Objects.equals(this.postingParty, other.postingParty) &&
        Objects.equals(this.valuedAt, other.valuedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.asset, this.haircut, this.positionId, this.postingParty,
        this.valuedAt);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition(%s, %s, %s, %s, %s)",
        this.asset, this.haircut, this.positionId, this.postingParty, this.valuedAt);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<CollateralPosition> get() {
      return jsonDecoder();
    }
  }
}
