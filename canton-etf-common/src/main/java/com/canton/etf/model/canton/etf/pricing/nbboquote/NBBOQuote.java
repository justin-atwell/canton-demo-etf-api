package com.canton.etf.model.canton.etf.pricing.nbboquote;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.da.internal.template.Archive;
import com.daml.ledger.javaapi.data.ContractFilter;
import com.daml.ledger.javaapi.data.CreateAndExerciseCommand;
import com.daml.ledger.javaapi.data.CreateCommand;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.ExerciseCommand;
import com.daml.ledger.javaapi.data.Identifier;
import com.daml.ledger.javaapi.data.Int64;
import com.daml.ledger.javaapi.data.Numeric;
import com.daml.ledger.javaapi.data.PackageVersion;
import com.daml.ledger.javaapi.data.Party;
import com.daml.ledger.javaapi.data.Template;
import com.daml.ledger.javaapi.data.Text;
import com.daml.ledger.javaapi.data.Timestamp;
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
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class NBBOQuote extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Pricing.NBBOQuote", "NBBOQuote");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18", "Canton.ETF.Pricing.NBBOQuote", "NBBOQuote");

  public static final String PACKAGE_ID = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<NBBOQuote, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<NBBOQuote, Consume, Unit> CHOICE_Consume = 
      Choice.create("Consume", value$ -> value$.toValue(), value$ -> Consume.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Consume.JsonDecoder$().get(), JsonLfDecoders.unit, Consume::jsonEncoder,
        JsonLfEncoders::unit);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, NBBOQuote> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(NBBOQuote.PACKAGE_ID, NBBOQuote.PACKAGE_NAME, NBBOQuote.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.pricing.nbboquote.NBBOQuote", TEMPLATE_ID, ContractId::new,
        v -> NBBOQuote.templateValueDecoder().decode(v), NBBOQuote::fromJson, Contract::new,
        List.of(CHOICE_Archive, CHOICE_Consume));

  public final String marketMaker;

  public final String fundManager;

  public final String compliance;

  public final String auditor;

  public final String symbol;

  public final BigDecimal bidPrice;

  public final BigDecimal askPrice;

  public final Long bidSize;

  public final Long askSize;

  public final Instant timestamp;

  public NBBOQuote(String marketMaker, String fundManager, String compliance, String auditor,
      String symbol, BigDecimal bidPrice, BigDecimal askPrice, Long bidSize, Long askSize,
      Instant timestamp) {
    this.marketMaker = marketMaker;
    this.fundManager = fundManager;
    this.compliance = compliance;
    this.auditor = auditor;
    this.symbol = symbol;
    this.bidPrice = bidPrice;
    this.askPrice = askPrice;
    this.bidSize = bidSize;
    this.askSize = askSize;
    this.timestamp = timestamp;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(NBBOQuote.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseConsume} instead
   */
  @Deprecated
  public Update<Exercised<Unit>> createAndExerciseConsume(Consume arg) {
    return createAnd().exerciseConsume(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseConsume} instead
   */
  @Deprecated
  public Update<Exercised<Unit>> createAndExerciseConsume() {
    return createAndExerciseConsume(new Consume());
  }

  public static Update<Created<ContractId>> create(String marketMaker, String fundManager,
      String compliance, String auditor, String symbol, BigDecimal bidPrice, BigDecimal askPrice,
      Long bidSize, Long askSize, Instant timestamp) {
    return new NBBOQuote(marketMaker, fundManager, compliance, auditor, symbol, bidPrice, askPrice,
        bidSize, askSize, timestamp).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, NBBOQuote> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<NBBOQuote> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(10);
    fields.add(new DamlRecord.Field("marketMaker", new Party(this.marketMaker)));
    fields.add(new DamlRecord.Field("fundManager", new Party(this.fundManager)));
    fields.add(new DamlRecord.Field("compliance", new Party(this.compliance)));
    fields.add(new DamlRecord.Field("auditor", new Party(this.auditor)));
    fields.add(new DamlRecord.Field("symbol", new Text(this.symbol)));
    fields.add(new DamlRecord.Field("bidPrice", new Numeric(this.bidPrice)));
    fields.add(new DamlRecord.Field("askPrice", new Numeric(this.askPrice)));
    fields.add(new DamlRecord.Field("bidSize", new Int64(this.bidSize)));
    fields.add(new DamlRecord.Field("askSize", new Int64(this.askSize)));
    fields.add(new DamlRecord.Field("timestamp", Timestamp.fromInstant(this.timestamp)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<NBBOQuote> templateValueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(10,0, recordValue$);
      String marketMaker = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String fundManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String compliance = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String auditor = PrimitiveValueDecoders.fromParty.decode(fields$.get(3).getValue());
      String symbol = PrimitiveValueDecoders.fromText.decode(fields$.get(4).getValue());
      BigDecimal bidPrice = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(5).getValue());
      BigDecimal askPrice = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(6).getValue());
      Long bidSize = PrimitiveValueDecoders.fromInt64.decode(fields$.get(7).getValue());
      Long askSize = PrimitiveValueDecoders.fromInt64.decode(fields$.get(8).getValue());
      Instant timestamp = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(9).getValue());
      return new NBBOQuote(marketMaker, fundManager, compliance, auditor, symbol, bidPrice,
          askPrice, bidSize, askSize, timestamp);
    } ;
  }

  public static JsonLfDecoder<NBBOQuote> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("marketMaker", "fundManager", "compliance", "auditor", "symbol", "bidPrice", "askPrice", "bidSize", "askSize", "timestamp"), name -> {
          switch (name) {
            case "marketMaker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "fundManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "compliance": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "auditor": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "symbol": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "bidPrice": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "askPrice": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "bidSize": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.int64);
            case "askSize": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(8, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.int64);
            case "timestamp": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(9, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new NBBOQuote(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7]), JsonLfDecoders.cast(args[8]), JsonLfDecoders.cast(args[9])));
  }

  public static NBBOQuote fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("marketMaker", apply(JsonLfEncoders::party, marketMaker)),
        JsonLfEncoders.Field.of("fundManager", apply(JsonLfEncoders::party, fundManager)),
        JsonLfEncoders.Field.of("compliance", apply(JsonLfEncoders::party, compliance)),
        JsonLfEncoders.Field.of("auditor", apply(JsonLfEncoders::party, auditor)),
        JsonLfEncoders.Field.of("symbol", apply(JsonLfEncoders::text, symbol)),
        JsonLfEncoders.Field.of("bidPrice", apply(JsonLfEncoders::numeric, bidPrice)),
        JsonLfEncoders.Field.of("askPrice", apply(JsonLfEncoders::numeric, askPrice)),
        JsonLfEncoders.Field.of("bidSize", apply(JsonLfEncoders::int64, bidSize)),
        JsonLfEncoders.Field.of("askSize", apply(JsonLfEncoders::int64, askSize)),
        JsonLfEncoders.Field.of("timestamp", apply(JsonLfEncoders::timestamp, timestamp)));
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
    if (!(object instanceof NBBOQuote)) {
      return false;
    }
    NBBOQuote other = (NBBOQuote) object;
    return Objects.equals(this.marketMaker, other.marketMaker) &&
        Objects.equals(this.fundManager, other.fundManager) &&
        Objects.equals(this.compliance, other.compliance) &&
        Objects.equals(this.auditor, other.auditor) && Objects.equals(this.symbol, other.symbol) &&
        Objects.equals(this.bidPrice, other.bidPrice) &&
        Objects.equals(this.askPrice, other.askPrice) &&
        Objects.equals(this.bidSize, other.bidSize) &&
        Objects.equals(this.askSize, other.askSize) &&
        Objects.equals(this.timestamp, other.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.marketMaker, this.fundManager, this.compliance, this.auditor,
        this.symbol, this.bidPrice, this.askPrice, this.bidSize, this.askSize, this.timestamp);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.pricing.nbboquote.NBBOQuote(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        this.marketMaker, this.fundManager, this.compliance, this.auditor, this.symbol,
        this.bidPrice, this.askPrice, this.bidSize, this.askSize, this.timestamp);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<NBBOQuote> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, NBBOQuote, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<NBBOQuote> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, NBBOQuote> {
    public Contract(ContractId id, NBBOQuote data, Set<String> signatories, Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, NBBOQuote> getCompanion() {
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

    default Update<Exercised<Unit>> exerciseConsume(Consume arg) {
      return makeExerciseCmd(CHOICE_Consume, arg);
    }

    default Update<Exercised<Unit>> exerciseConsume() {
      return exerciseConsume(new Consume());
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, NBBOQuote, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<NBBOQuote> get() {
      return jsonDecoder();
    }
  }
}
