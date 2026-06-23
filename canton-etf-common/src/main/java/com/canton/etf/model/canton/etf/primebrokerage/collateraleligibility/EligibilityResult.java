package com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility;

import com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.eligibilityresult.Eligible;
import com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.eligibilityresult.Ineligible;
import com.daml.ledger.javaapi.data.DamlRecord;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public abstract class EligibilityResult extends Variant<EligibilityResult> {
  public static final String _packageId = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public EligibilityResult() {
  }

  public abstract com.daml.ledger.javaapi.data.Variant toValue();

  public static ValueDecoder<EligibilityResult> valueDecoder() {
    return value$ -> {
      com.daml.ledger.javaapi.data.Variant variant$ = value$.asVariant().orElseThrow(() -> new IllegalArgumentException("Expected Variant to build an instance of the Variant com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.EligibilityResult"));
      if ("Eligible".equals(variant$.getConstructor())) {
        return valueDecoderEligible().decode(variant$);
      }
      if ("Ineligible".equals(variant$.getConstructor())) {
        return valueDecoderIneligible().decode(variant$);
      }
      throw new IllegalArgumentException("Found unknown constructor " + variant$.getConstructor() + " for variant com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.EligibilityResult, expected one of [Eligible, Ineligible]. This could be a failed variant downgrade.");
    } ;
  }

  public static JsonLfDecoder<EligibilityResult> jsonDecoder() {
    return JsonLfDecoders.variant(Arrays.asList("Eligible", "Ineligible"), name -> {
          switch (name) {
            case "Eligible": return jsonDecoderEligible();
            case "Ineligible": return jsonDecoderIneligible();
            default: return null;
          }
        }
        );
  }

  public static EligibilityResult fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  protected abstract JsonLfEncoders.Field fieldForJsonEncoder();

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.variant(EligibilityResult::fieldForJsonEncoder).apply(this);
  }

  public static ValueDecoder<Ineligible> valueDecoderIneligible() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = PrimitiveValueDecoders.variantCheck("Ineligible", value$);
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(1,0, recordValue$);
      String reason = PrimitiveValueDecoders.fromText.decode(fields$.get(0).getValue());
      return new Ineligible(reason);
    } ;
  }

  private static JsonLfDecoder<Ineligible> jsonDecoderIneligible() {
    return JsonLfDecoders.record(Arrays.asList("reason"), name -> {
          switch (name) {
            case "reason": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            default: return null;
          }
        }
        , (Object[] args) -> new Ineligible(JsonLfDecoders.cast(args[0])));
  }

  public static ValueDecoder<Eligible> valueDecoderEligible() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = PrimitiveValueDecoders.variantCheck("Eligible", value$);
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(1,0, recordValue$);
      BigDecimal haircutAdjustedValue = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(0).getValue());
      return new Eligible(haircutAdjustedValue);
    } ;
  }

  private static JsonLfDecoder<Eligible> jsonDecoderEligible() {
    return JsonLfDecoders.record(Arrays.asList("haircutAdjustedValue"), name -> {
          switch (name) {
            case "haircutAdjustedValue": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new Eligible(JsonLfDecoders.cast(args[0])));
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<EligibilityResult> get() {
      return jsonDecoder();
    }
  }
}
