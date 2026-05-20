package com.canton.etf.model.canton.etf.fund.captable;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.da.internal.template.Archive;
import com.canton.etf.model.da.types.Tuple2;
import com.daml.ledger.javaapi.data.ContractFilter;
import com.daml.ledger.javaapi.data.CreateAndExerciseCommand;
import com.daml.ledger.javaapi.data.CreateCommand;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.DamlCollectors;
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

public final class CapTable extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Fund.CapTable", "CapTable");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("1f28d7b7af3161c75a78b37a77e89a8c08d9579c841613879009c4365fd6566c", "Canton.ETF.Fund.CapTable", "CapTable");

  public static final String PACKAGE_ID = "1f28d7b7af3161c75a78b37a77e89a8c08d9579c841613879009c4365fd6566c";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<CapTable, IssueShares, ContractId> CHOICE_IssueShares = 
      Choice.create("IssueShares", value$ -> value$.toValue(), value$ -> IssueShares.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new IssueShares.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        IssueShares::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<CapTable, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<CapTable, RedeemShares, ContractId> CHOICE_RedeemShares = 
      Choice.create("RedeemShares", value$ -> value$.toValue(), value$ ->
        RedeemShares.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new RedeemShares.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        RedeemShares::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, CapTable> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(CapTable.PACKAGE_ID, CapTable.PACKAGE_NAME, CapTable.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.fund.captable.CapTable", TEMPLATE_ID, ContractId::new,
        v -> CapTable.templateValueDecoder().decode(v), CapTable::fromJson, Contract::new,
        List.of(CHOICE_IssueShares, CHOICE_Archive, CHOICE_RedeemShares));

  public final String fundManager;

  public final String custodian;

  public final String ticker;

  public final BigDecimal totalShares;

  public final List<Tuple2<String, BigDecimal>> entries;

  public CapTable(String fundManager, String custodian, String ticker, BigDecimal totalShares,
      List<Tuple2<String, BigDecimal>> entries) {
    this.fundManager = fundManager;
    this.custodian = custodian;
    this.ticker = ticker;
    this.totalShares = totalShares;
    this.entries = entries;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(CapTable.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseIssueShares} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseIssueShares(IssueShares arg) {
    return createAnd().exerciseIssueShares(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseIssueShares} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseIssueShares(String recipient,
      BigDecimal amount) {
    return createAndExerciseIssueShares(new IssueShares(recipient, amount));
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRedeemShares} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRedeemShares(RedeemShares arg) {
    return createAnd().exerciseRedeemShares(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRedeemShares} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRedeemShares(String recipient,
      BigDecimal amount) {
    return createAndExerciseRedeemShares(new RedeemShares(recipient, amount));
  }

  public static Update<Created<ContractId>> create(String fundManager, String custodian,
      String ticker, BigDecimal totalShares, List<Tuple2<String, BigDecimal>> entries) {
    return new CapTable(fundManager, custodian, ticker, totalShares, entries).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, CapTable> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<CapTable> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(5);
    fields.add(new DamlRecord.Field("fundManager", new Party(this.fundManager)));
    fields.add(new DamlRecord.Field("custodian", new Party(this.custodian)));
    fields.add(new DamlRecord.Field("ticker", new Text(this.ticker)));
    fields.add(new DamlRecord.Field("totalShares", new Numeric(this.totalShares)));
    fields.add(new DamlRecord.Field("entries", this.entries.stream().collect(DamlCollectors.toDamlList(v$0 -> v$0.toValue(v$1 -> new Party(v$1),
        v$2 -> new Numeric(v$2))))));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<CapTable> templateValueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(5,0, recordValue$);
      String fundManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String custodian = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String ticker = PrimitiveValueDecoders.fromText.decode(fields$.get(2).getValue());
      BigDecimal totalShares = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(3).getValue());
      List<Tuple2<String, BigDecimal>> entries = PrimitiveValueDecoders.fromList(
            Tuple2.<java.lang.String,
            java.math.BigDecimal>valueDecoder(PrimitiveValueDecoders.fromParty,
            PrimitiveValueDecoders.fromNumeric)).decode(fields$.get(4).getValue());
      return new CapTable(fundManager, custodian, ticker, totalShares, entries);
    } ;
  }

  public static JsonLfDecoder<CapTable> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("fundManager", "custodian", "ticker", "totalShares", "entries"), name -> {
          switch (name) {
            case "fundManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "custodian": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "ticker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "totalShares": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "entries": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.list(new com.canton.etf.model.da.types.Tuple2.JsonDecoder$().get(com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10))));
            default: return null;
          }
        }
        , (Object[] args) -> new CapTable(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4])));
  }

  public static CapTable fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("fundManager", apply(JsonLfEncoders::party, fundManager)),
        JsonLfEncoders.Field.of("custodian", apply(JsonLfEncoders::party, custodian)),
        JsonLfEncoders.Field.of("ticker", apply(JsonLfEncoders::text, ticker)),
        JsonLfEncoders.Field.of("totalShares", apply(JsonLfEncoders::numeric, totalShares)),
        JsonLfEncoders.Field.of("entries", apply(JsonLfEncoders.list(_x1 -> _x1.jsonEncoder(JsonLfEncoders::party, JsonLfEncoders::numeric)), entries)));
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
    if (!(object instanceof CapTable)) {
      return false;
    }
    CapTable other = (CapTable) object;
    return Objects.equals(this.fundManager, other.fundManager) &&
        Objects.equals(this.custodian, other.custodian) &&
        Objects.equals(this.ticker, other.ticker) &&
        Objects.equals(this.totalShares, other.totalShares) &&
        Objects.equals(this.entries, other.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fundManager, this.custodian, this.ticker, this.totalShares,
        this.entries);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.fund.captable.CapTable(%s, %s, %s, %s, %s)",
        this.fundManager, this.custodian, this.ticker, this.totalShares, this.entries);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<CapTable> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CapTable, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<CapTable> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, CapTable> {
    public Contract(ContractId id, CapTable data, Set<String> signatories, Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, CapTable> getCompanion() {
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
    default Update<Exercised<ContractId>> exerciseIssueShares(IssueShares arg) {
      return makeExerciseCmd(CHOICE_IssueShares, arg);
    }

    default Update<Exercised<ContractId>> exerciseIssueShares(String recipient, BigDecimal amount) {
      return exerciseIssueShares(new IssueShares(recipient, amount));
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<ContractId>> exerciseRedeemShares(RedeemShares arg) {
      return makeExerciseCmd(CHOICE_RedeemShares, arg);
    }

    default Update<Exercised<ContractId>> exerciseRedeemShares(String recipient,
        BigDecimal amount) {
      return exerciseRedeemShares(new RedeemShares(recipient, amount));
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CapTable, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<CapTable> get() {
      return jsonDecoder();
    }
  }
}
