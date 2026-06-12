package com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall;

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

public enum WaterfallStatus implements DamlEnum<WaterfallStatus> {
  INPROGRESS,

  SHORTFALLCOVERED,

  COLLATERALEXHAUSTED;

  private static final com.daml.ledger.javaapi.data.DamlEnum[] __values$ = {new com.daml.ledger.javaapi.data.DamlEnum("InProgress"), new com.daml.ledger.javaapi.data.DamlEnum("ShortfallCovered"), new com.daml.ledger.javaapi.data.DamlEnum("CollateralExhausted")};

  private static final Map<String, WaterfallStatus> __enums$ = __buildEnumsMap$();

  private static final Map<String, WaterfallStatus> __buildEnumsMap$() {
    Map<String, WaterfallStatus> m = new HashMap<String, WaterfallStatus>();
    m.put("InProgress", INPROGRESS);
    m.put("ShortfallCovered", SHORTFALLCOVERED);
    m.put("CollateralExhausted", COLLATERALEXHAUSTED);
    return m;
  }

  public static final ValueDecoder<WaterfallStatus> valueDecoder() {
    return value$ -> {
      String constructor$ = value$.asEnum().orElseThrow(() -> new IllegalArgumentException("Expected DamlEnum to build an instance of the Enum com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.WaterfallStatus")).getConstructor();
      if (!__enums$.containsKey(constructor$)) throw new IllegalArgumentException("Found unknown constructor " + constructor$ + " for enum com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.WaterfallStatus, expected one of [InProgress, ShortfallCovered, CollateralExhausted]. This could be a failed enum downgrade.");
      return __enums$.get(constructor$);
    } ;
  }

  public final com.daml.ledger.javaapi.data.DamlEnum toValue() {
    return __values$[ordinal()];
  }

  public static JsonLfDecoder<WaterfallStatus> jsonDecoder() {
    return JsonLfDecoders.enumeration(__enums$);
  }

  public static WaterfallStatus fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public String getConstructor() {
    return toValue().getConstructor();
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.enumeration((WaterfallStatus e$) -> e$.getConstructor()).apply(this);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<WaterfallStatus> get() {
      return jsonDecoder();
    }
  }
}
