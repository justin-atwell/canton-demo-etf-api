package com.canton.etf.model.canton.etf.fund.constituent;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.da.internal.template.Archive;
import com.daml.ledger.javaapi.data.ContractFilter;
import com.daml.ledger.javaapi.data.CreateAndExerciseCommand;
import com.daml.ledger.javaapi.data.CreateCommand;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.ExerciseCommand;
import com.daml.ledger.javaapi.data.Identifier;
import com.daml.ledger.javaapi.data.Numeric;
import com.daml.ledger.javaapi.data.PackageVersion;
import com.daml.ledger.javaapi.data.Party;
import com.daml.ledger.javaapi.data.Template;
import com.daml.ledger.javaapi.data.Text;
import com.daml.ledger.javaapi.data.Unit;
import com.daml.ledger.javaapi.data.Value;
import com.daml.ledger.javaapi.data.codegen.Choice;
import com.daml.ledger.javaapi.data.codegen.ContractCompanion;
import com.daml.ledger.javaapi.data.codegen.ContractTypeCompanion;
import com.daml.ledger.javaapi.data.codegen.Created;
import com.daml.ledger.javaapi.data.codegen.Exercised;
import com.daml.ledger.javaapi.data.codegen.PrimitiveValueDecoders;
import com.daml.ledger.javaapi.data.codegen.Update;
import com.daml.ledger.javaapi.data.codegen.ValueDecoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoder;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders;
import com.daml.ledger.javaapi.data.codegen.json.JsonLfReader;
import java.lang.Deprecated;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Constituent extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Fund.Constituent", "Constituent");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("c2e41b9e13dba8a6699eb8b7cde2c5e5c96ac00b38b18aa67373f159da67c93d", "Canton.ETF.Fund.Constituent", "Constituent");

  public static final String PACKAGE_ID = "c2e41b9e13dba8a6699eb8b7cde2c5e5c96ac00b38b18aa67373f159da67c93d";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<Constituent, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<Constituent, UpdateWeight, ContractId> CHOICE_UpdateWeight = 
      Choice.create("UpdateWeight", value$ -> value$.toValue(), value$ ->
        UpdateWeight.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new UpdateWeight.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        UpdateWeight::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, Constituent> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(Constituent.PACKAGE_ID, Constituent.PACKAGE_NAME, Constituent.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.fund.constituent.Constituent", TEMPLATE_ID,
        ContractId::new, v -> Constituent.templateValueDecoder().decode(v), Constituent::fromJson,
        Contract::new, List.of(CHOICE_Archive, CHOICE_UpdateWeight));

  public final String fundManager;

  public final String ticker;

  public final String symbol;

  public final String name;

  public final String cusip;

  public final BigDecimal weight;

  public Constituent(String fundManager, String ticker, String symbol, String name, String cusip,
      BigDecimal weight) {
    this.fundManager = fundManager;
    this.ticker = ticker;
    this.symbol = symbol;
    this.name = name;
    this.cusip = cusip;
    this.weight = weight;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(Constituent.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseArchive} instead
   */
  @Deprecated
  public Update<Exercised<Unit>> createAndExerciseArchive(Archive arg) {
    return createAnd().exerciseArchive(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseArchive} instead
   */
  @Deprecated
  public Update<Exercised<Unit>> createAndExerciseArchive() {
    return createAndExerciseArchive(new Archive());
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateWeight} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateWeight(UpdateWeight arg) {
    return createAnd().exerciseUpdateWeight(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateWeight} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateWeight(BigDecimal newWeight) {
    return createAndExerciseUpdateWeight(new UpdateWeight(newWeight));
  }

  public static Update<Created<ContractId>> create(String fundManager, String ticker, String symbol,
      String name, String cusip, BigDecimal weight) {
    return new Constituent(fundManager, ticker, symbol, name, cusip, weight).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, Constituent> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<Constituent> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(6);
    fields.add(new DamlRecord.Field("fundManager", new Party(this.fundManager)));
    fields.add(new DamlRecord.Field("ticker", new Text(this.ticker)));
    fields.add(new DamlRecord.Field("symbol", new Text(this.symbol)));
    fields.add(new DamlRecord.Field("name", new Text(this.name)));
    fields.add(new DamlRecord.Field("cusip", new Text(this.cusip)));
    fields.add(new DamlRecord.Field("weight", new Numeric(this.weight)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<Constituent> templateValueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(6,0, recordValue$);
      String fundManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String ticker = PrimitiveValueDecoders.fromText.decode(fields$.get(1).getValue());
      String symbol = PrimitiveValueDecoders.fromText.decode(fields$.get(2).getValue());
      String name = PrimitiveValueDecoders.fromText.decode(fields$.get(3).getValue());
      String cusip = PrimitiveValueDecoders.fromText.decode(fields$.get(4).getValue());
      BigDecimal weight = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(5).getValue());
      return new Constituent(fundManager, ticker, symbol, name, cusip, weight);
    } ;
  }

  public static JsonLfDecoder<Constituent> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("fundManager", "ticker", "symbol", "name", "cusip", "weight"), name -> {
          switch (name) {
            case "fundManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "ticker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "symbol": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "name": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "cusip": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "weight": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new Constituent(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5])));
  }

  public static Constituent fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("fundManager", apply(JsonLfEncoders::party, fundManager)),
        JsonLfEncoders.Field.of("ticker", apply(JsonLfEncoders::text, ticker)),
        JsonLfEncoders.Field.of("symbol", apply(JsonLfEncoders::text, symbol)),
        JsonLfEncoders.Field.of("name", apply(JsonLfEncoders::text, name)),
        JsonLfEncoders.Field.of("cusip", apply(JsonLfEncoders::text, cusip)),
        JsonLfEncoders.Field.of("weight", apply(JsonLfEncoders::numeric, weight)));
  }

  public static ContractFilter<Contract> contractFilter() {
    return ContractFilter.of(COMPANION);
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (!(object instanceof Constituent)) {
      return false;
    }
    Constituent other = (Constituent) object;
    return Objects.equals(this.fundManager, other.fundManager) &&
        Objects.equals(this.ticker, other.ticker) && Objects.equals(this.symbol, other.symbol) &&
        Objects.equals(this.name, other.name) && Objects.equals(this.cusip, other.cusip) &&
        Objects.equals(this.weight, other.weight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fundManager, this.ticker, this.symbol, this.name, this.cusip,
        this.weight);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.fund.constituent.Constituent(%s, %s, %s, %s, %s, %s)",
        this.fundManager, this.ticker, this.symbol, this.name, this.cusip, this.weight);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<Constituent> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, Constituent, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<Constituent> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, Constituent> {
    public Contract(ContractId id, Constituent data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, Constituent> getCompanion() {
      return COMPANION;
    }

    public static Contract fromIdAndRecord(String contractId, DamlRecord record$,
        Set<String> signatories, Set<String> observers) {
      return COMPANION.fromIdAndRecord(contractId, record$, signatories, observers);
    }

    public static Contract fromCreatedEvent(CreatedEvent event) {
      return COMPANION.fromCreatedEvent(event);
    }
  }

  public interface Exercises<Cmd> extends com.daml.ledger.javaapi.data.codegen.Exercises.Archivable<Cmd> {
    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<ContractId>> exerciseUpdateWeight(UpdateWeight arg) {
      return makeExerciseCmd(CHOICE_UpdateWeight, arg);
    }

    default Update<Exercised<ContractId>> exerciseUpdateWeight(BigDecimal newWeight) {
      return exerciseUpdateWeight(new UpdateWeight(newWeight));
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, Constituent, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<Constituent> get() {
      return jsonDecoder();
    }
  }
}
