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

public enum ResponseType implements DamlEnum<ResponseType> {
  POSTADDITIONALCOLLATERAL,

  INITIATESUBSTITUTION,

  ACCEPTPARTIALLIQUIDATION;

  private static final com.daml.ledger.javaapi.data.DamlEnum[] __values$ = {new com.daml.ledger.javaapi.data.DamlEnum("PostAdditionalCollateral"), new com.daml.ledger.javaapi.data.DamlEnum("InitiateSubstitution"), new com.daml.ledger.javaapi.data.DamlEnum("AcceptPartialLiquidation")};

  private static final Map<String, ResponseType> __enums$ = __buildEnumsMap$();

  private static final Map<String, ResponseType> __buildEnumsMap$() {
    Map<String, ResponseType> m = new HashMap<String, ResponseType>();
    m.put("PostAdditionalCollateral", POSTADDITIONALCOLLATERAL);
    m.put("InitiateSubstitution", INITIATESUBSTITUTION);
    m.put("AcceptPartialLiquidation", ACCEPTPARTIALLIQUIDATION);
    return m;
  }

  public static final ValueDecoder<ResponseType> valueDecoder() {
    return value$ -> {
      String constructor$ = value$.asEnum().orElseThrow(() -> new IllegalArgumentException("Expected DamlEnum to build an instance of the Enum com.canton.etf.model.canton.etf.primebrokerage.margincallv2.ResponseType")).getConstructor();
      if (!__enums$.containsKey(constructor$)) throw new IllegalArgumentException("Found unknown constructor " + constructor$ + " for enum com.canton.etf.model.canton.etf.primebrokerage.margincallv2.ResponseType, expected one of [PostAdditionalCollateral, InitiateSubstitution, AcceptPartialLiquidation]. This could be a failed enum downgrade.");
      return __enums$.get(constructor$);
    } ;
  }

  public final com.daml.ledger.javaapi.data.DamlEnum toValue() {
    return __values$[ordinal()];
  }

  public static JsonLfDecoder<ResponseType> jsonDecoder() {
    return JsonLfDecoders.enumeration(__enums$);
  }

  public static ResponseType fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public String getConstructor() {
    return toValue().getConstructor();
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.enumeration((ResponseType e$) -> e$.getConstructor()).apply(this);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<ResponseType> get() {
      return jsonDecoder();
    }
  }
}
