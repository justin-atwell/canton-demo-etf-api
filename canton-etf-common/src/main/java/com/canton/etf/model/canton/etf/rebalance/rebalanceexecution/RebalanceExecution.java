package com.canton.etf.model.canton.etf.rebalance.rebalanceexecution;

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

public final class RebalanceExecution extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Rebalance.RebalanceExecution", "RebalanceExecution");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18", "Canton.ETF.Rebalance.RebalanceExecution", "RebalanceExecution");

  public static final String PACKAGE_ID = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<RebalanceExecution, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, RebalanceExecution> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(RebalanceExecution.PACKAGE_ID, RebalanceExecution.PACKAGE_NAME, RebalanceExecution.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.rebalance.rebalanceexecution.RebalanceExecution",
        TEMPLATE_ID, ContractId::new, v -> RebalanceExecution.templateValueDecoder().decode(v),
        RebalanceExecution::fromJson, Contract::new, List.of(CHOICE_Archive));

  public final String fundManager;

  public final String custodian;

  public final String compliance;

  public final String auditor;

  public final String ticker;

  public final String proposalId;

  public final List<Tuple2<String, BigDecimal>> executedWeights;

  public final Instant executedAt;

  public RebalanceExecution(String fundManager, String custodian, String compliance, String auditor,
      String ticker, String proposalId, List<Tuple2<String, BigDecimal>> executedWeights,
      Instant executedAt) {
    this.fundManager = fundManager;
    this.custodian = custodian;
    this.compliance = compliance;
    this.auditor = auditor;
    this.ticker = ticker;
    this.proposalId = proposalId;
    this.executedWeights = executedWeights;
    this.executedAt = executedAt;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(RebalanceExecution.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
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

  public static Update<Created<ContractId>> create(String fundManager, String custodian,
      String compliance, String auditor, String ticker, String proposalId,
      List<Tuple2<String, BigDecimal>> executedWeights, Instant executedAt) {
    return new RebalanceExecution(fundManager, custodian, compliance, auditor, ticker, proposalId,
        executedWeights, executedAt).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, RebalanceExecution> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<RebalanceExecution> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(8);
    fields.add(new DamlRecord.Field("fundManager", new Party(this.fundManager)));
    fields.add(new DamlRecord.Field("custodian", new Party(this.custodian)));
    fields.add(new DamlRecord.Field("compliance", new Party(this.compliance)));
    fields.add(new DamlRecord.Field("auditor", new Party(this.auditor)));
    fields.add(new DamlRecord.Field("ticker", new Text(this.ticker)));
    fields.add(new DamlRecord.Field("proposalId", new Text(this.proposalId)));
    fields.add(new DamlRecord.Field("executedWeights", this.executedWeights.stream().collect(DamlCollectors.toDamlList(v$0 -> v$0.toValue(v$1 -> new Text(v$1),
        v$2 -> new Numeric(v$2))))));
    fields.add(new DamlRecord.Field("executedAt", Timestamp.fromInstant(this.executedAt)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<RebalanceExecution> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(8,0, recordValue$);
      String fundManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String custodian = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String compliance = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String auditor = PrimitiveValueDecoders.fromParty.decode(fields$.get(3).getValue());
      String ticker = PrimitiveValueDecoders.fromText.decode(fields$.get(4).getValue());
      String proposalId = PrimitiveValueDecoders.fromText.decode(fields$.get(5).getValue());
      List<Tuple2<String, BigDecimal>> executedWeights = PrimitiveValueDecoders.fromList(
            Tuple2.<java.lang.String,
            java.math.BigDecimal>valueDecoder(PrimitiveValueDecoders.fromText,
            PrimitiveValueDecoders.fromNumeric)).decode(fields$.get(6).getValue());
      Instant executedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(7).getValue());
      return new RebalanceExecution(fundManager, custodian, compliance, auditor, ticker, proposalId,
          executedWeights, executedAt);
    } ;
  }

  public static JsonLfDecoder<RebalanceExecution> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("fundManager", "custodian", "compliance", "auditor", "ticker", "proposalId", "executedWeights", "executedAt"), name -> {
          switch (name) {
            case "fundManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "custodian": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "compliance": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "auditor": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "ticker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "proposalId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "executedWeights": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.list(new com.canton.etf.model.da.types.Tuple2.JsonDecoder$().get(com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10))));
            case "executedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new RebalanceExecution(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7])));
  }

  public static RebalanceExecution fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("fundManager", apply(JsonLfEncoders::party, fundManager)),
        JsonLfEncoders.Field.of("custodian", apply(JsonLfEncoders::party, custodian)),
        JsonLfEncoders.Field.of("compliance", apply(JsonLfEncoders::party, compliance)),
        JsonLfEncoders.Field.of("auditor", apply(JsonLfEncoders::party, auditor)),
        JsonLfEncoders.Field.of("ticker", apply(JsonLfEncoders::text, ticker)),
        JsonLfEncoders.Field.of("proposalId", apply(JsonLfEncoders::text, proposalId)),
        JsonLfEncoders.Field.of("executedWeights", apply(JsonLfEncoders.list(_x1 -> _x1.jsonEncoder(JsonLfEncoders::text, JsonLfEncoders::numeric)), executedWeights)),
        JsonLfEncoders.Field.of("executedAt", apply(JsonLfEncoders::timestamp, executedAt)));
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
    if (!(object instanceof RebalanceExecution)) {
      return false;
    }
    RebalanceExecution other = (RebalanceExecution) object;
    return Objects.equals(this.fundManager, other.fundManager) &&
        Objects.equals(this.custodian, other.custodian) &&
        Objects.equals(this.compliance, other.compliance) &&
        Objects.equals(this.auditor, other.auditor) && Objects.equals(this.ticker, other.ticker) &&
        Objects.equals(this.proposalId, other.proposalId) &&
        Objects.equals(this.executedWeights, other.executedWeights) &&
        Objects.equals(this.executedAt, other.executedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fundManager, this.custodian, this.compliance, this.auditor,
        this.ticker, this.proposalId, this.executedWeights, this.executedAt);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.rebalance.rebalanceexecution.RebalanceExecution(%s, %s, %s, %s, %s, %s, %s, %s)",
        this.fundManager, this.custodian, this.compliance, this.auditor, this.ticker,
        this.proposalId, this.executedWeights, this.executedAt);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<RebalanceExecution> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, RebalanceExecution, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<RebalanceExecution> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, RebalanceExecution> {
    public Contract(ContractId id, RebalanceExecution data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, RebalanceExecution> getCompanion() {
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
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, RebalanceExecution, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<RebalanceExecution> get() {
      return jsonDecoder();
    }
  }
}
