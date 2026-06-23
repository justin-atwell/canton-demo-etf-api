package com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest;

import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus.ApprovedByBroker;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus.Completed;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus.Pending;
import com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.substitutionstatus.Rejected;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.Unit;
import com.daml.ledger.javaapi.data.Value;
import com.daml.ledger.javaapi.data.codegen.PrimitiveValueDecoders;
import com.daml.ledger.javaapi.data.codegen.ValueDecoder;
import com.daml.ledger.javaapi.data.codegen.Variant;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfReader;
import java.lang.IllegalArgumentException;
import java.lang.String;
import java.util.Arrays;
import java.util.List;

public abstract class SubstitutionStatus extends Variant<SubstitutionStatus> {
  public static final String _packageId = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public SubstitutionStatus() {
  }

  public abstract com.daml.ledger.javaapi.data.Variant toValue();

  public static ValueDecoder<SubstitutionStatus> valueDecoder() {
    return value$ -> {
      com.daml.ledger.javaapi.data.Variant variant$ = value$.asVariant().orElseThrow(() -> new IllegalArgumentException("Expected Variant to build an instance of the Variant com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionStatus"));
      if ("Pending".equals(variant$.getConstructor())) {
        return valueDecoderPending().decode(variant$);
      }
      if ("ApprovedByBroker".equals(variant$.getConstructor())) {
        return valueDecoderApprovedByBroker().decode(variant$);
      }
      if ("Completed".equals(variant$.getConstructor())) {
        return valueDecoderCompleted().decode(variant$);
      }
      if ("Rejected".equals(variant$.getConstructor())) {
        return valueDecoderRejected().decode(variant$);
      }
      throw new IllegalArgumentException("Found unknown constructor " + variant$.getConstructor() + " for variant com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionStatus, expected one of [Pending, ApprovedByBroker, Completed, Rejected]. This could be a failed variant downgrade.");
    } ;
  }

  public static JsonLfDecoder<SubstitutionStatus> jsonDecoder() {
    return JsonLfDecoders.variant(Arrays.asList("Pending", "ApprovedByBroker", "Completed", "Rejected"), name -> {
          switch (name) {
            case "Pending": return jsonDecoderPending();
            case "ApprovedByBroker": return jsonDecoderApprovedByBroker();
            case "Completed": return jsonDecoderCompleted();
            case "Rejected": return jsonDecoderRejected();
            default: return null;
          }
        }
        );
  }

  public static SubstitutionStatus fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  protected abstract JsonLfEncoders.Field fieldForJsonEncoder();

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.variant(SubstitutionStatus::fieldForJsonEncoder).apply(this);
  }

  private static ValueDecoder<Pending> valueDecoderPending() throws IllegalArgumentException {
    return value$ -> {
      Value variantValue$ = PrimitiveValueDecoders.variantCheck("Pending", value$);
      Unit body = PrimitiveValueDecoders.fromUnit.decode(variantValue$);
      return new Pending(body);
    } ;
  }

  private static JsonLfDecoder<Pending> jsonDecoderPending() {
    return r -> new Pending(JsonLfDecoders.unit.decode(r));
  }

  private static ValueDecoder<ApprovedByBroker> valueDecoderApprovedByBroker() throws
      IllegalArgumentException {
    return value$ -> {
      Value variantValue$ = PrimitiveValueDecoders.variantCheck("ApprovedByBroker", value$);
      Unit body = PrimitiveValueDecoders.fromUnit.decode(variantValue$);
      return new ApprovedByBroker(body);
    } ;
  }

  private static JsonLfDecoder<ApprovedByBroker> jsonDecoderApprovedByBroker() {
    return r -> new ApprovedByBroker(JsonLfDecoders.unit.decode(r));
  }

  private static ValueDecoder<Completed> valueDecoderCompleted() throws IllegalArgumentException {
    return value$ -> {
      Value variantValue$ = PrimitiveValueDecoders.variantCheck("Completed", value$);
      Unit body = PrimitiveValueDecoders.fromUnit.decode(variantValue$);
      return new Completed(body);
    } ;
  }

  private static JsonLfDecoder<Completed> jsonDecoderCompleted() {
    return r -> new Completed(JsonLfDecoders.unit.decode(r));
  }

  public static ValueDecoder<Rejected> valueDecoderRejected() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = PrimitiveValueDecoders.variantCheck("Rejected", value$);
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(1,0, recordValue$);
      String rejectionReason = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      return new Rejected(rejectionReason);
    } ;
  }

  private static JsonLfDecoder<Rejected> jsonDecoderRejected() {
    return JsonLfDecoders.record(Arrays.asList("rejectionReason"), name -> {
          switch (name) {
            case "rejectionReason": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            default: return null;
          }
        }
        , (Object[] args) -> new Rejected(JsonLfDecoders.cast(args[0])));
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<SubstitutionStatus> get() {
      return jsonDecoder();
    }
  }
}
