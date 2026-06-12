package com.canton.etf.model.canton.etf.primebrokerage.margincallv2;

import com.daml.ledger.javaapi.data.codegen.DamlEnum;
import com.daml.ledger.javaapi.data.codegen.ValueDecoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfReader;
import java.lang.IllegalArgumentException;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;

public enum MarginCallStatus implements DamlEnum<MarginCallStatus> {
  ISSUED,

  RESPONSERECEIVED,

  SATISFIED,

  DEFAULTED;

  private static final com.daml.ledger.javaapi.data.DamlEnum[] __values$ = {new com.daml.ledger.javaapi.data.DamlEnum("Issued"), new com.daml.ledger.javaapi.data.DamlEnum("ResponseReceived"), new com.daml.ledger.javaapi.data.DamlEnum("Satisfied"), new com.daml.ledger.javaapi.data.DamlEnum("Defaulted")};

  private static final Map<String, MarginCallStatus> __enums$ = __buildEnumsMap$();

  private static final Map<String, MarginCallStatus> __buildEnumsMap$() {
    Map<String, MarginCallStatus> m = new HashMap<String, MarginCallStatus>();
    m.put("Issued", ISSUED);
    m.put("ResponseReceived", RESPONSERECEIVED);
    m.put("Satisfied", SATISFIED);
    m.put("Defaulted", DEFAULTED);
    return m;
  }

  public static final ValueDecoder<MarginCallStatus> valueDecoder() {
    return value$ -> {
      String constructor$ = value$.asEnum().orElseThrow(() -> new IllegalArgumentException("Expected DamlEnum to build an instance of the Enum com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallStatus")).getConstructor();
      if (!__enums$.containsKey(constructor$)) throw new IllegalArgumentException("Found unknown constructor " + constructor$ + " for enum com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallStatus, expected one of [Issued, ResponseReceived, Satisfied, Defaulted]. This could be a failed enum downgrade.");
      return __enums$.get(constructor$);
    } ;
  }

  public final com.daml.ledger.javaapi.data.DamlEnum toValue() {
    return __values$[ordinal()];
  }

  public static JsonLfDecoder<MarginCallStatus> jsonDecoder() {
    return JsonLfDecoders.enumeration(__enums$);
  }

  public static MarginCallStatus fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public String getConstructor() {
    return toValue().getConstructor();
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.enumeration((MarginCallStatus e$) -> e$.getConstructor()).apply(this);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<MarginCallStatus> get() {
      return jsonDecoder();
    }
  }
}
