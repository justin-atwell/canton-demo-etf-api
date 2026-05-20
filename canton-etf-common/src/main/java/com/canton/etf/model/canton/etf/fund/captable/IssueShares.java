package com.canton.etf.model.canton.etf.fund.captable;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.daml.ledger.javaapi.data.Numeric;
import com.daml.ledger.javaapi.data.Party;
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

public class IssueShares extends DamlRecord<IssueShares> {
  public static final String _packageId = "1f28d7b7af3161c75a78b37a77e89a8c08d9579c841613879009c4365fd6566c";

  public final String recipient;

  public final BigDecimal amount;

  public IssueShares(String recipient, BigDecimal amount) {
    this.recipient = recipient;
    this.amount = amount;
  }

  public static ValueDecoder<IssueShares> valueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<com.daml.ledger.javaapi.data.DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(2,0,
          recordValue$);
      String recipient = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      BigDecimal amount = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(1).getValue());
      return new IssueShares(recipient, amount);
    } ;
  }

  public com.daml.ledger.javaapi.data.DamlRecord toValue() {
    ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field> fields = new ArrayList<com.daml.ledger.javaapi.data.DamlRecord.Field>(2);
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("recipient", new Party(this.recipient)));
    fields.add(new com.daml.ledger.javaapi.data.DamlRecord.Field("amount", new Numeric(this.amount)));
    return new com.daml.ledger.javaapi.data.DamlRecord(fields);
  }

  public static JsonLfDecoder<IssueShares> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("recipient", "amount"), name -> {
          switch (name) {
            case "recipient": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "amount": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new IssueShares(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1])));
  }

  public static IssueShares fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("recipient", apply(JsonLfEncoders::party, recipient)),
        JsonLfEncoders.Field.of("amount", apply(JsonLfEncoders::numeric, amount)));
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof IssueShares)) {
      return false;
    }
    IssueShares other = (IssueShares) object;
    return Objects.equals(this.recipient, other.recipient) &&
        Objects.equals(this.amount, other.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.recipient, this.amount);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.fund.captable.IssueShares(%s, %s)",
        this.recipient, this.amount);
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<IssueShares> get() {
      return jsonDecoder();
    }
  }
}
