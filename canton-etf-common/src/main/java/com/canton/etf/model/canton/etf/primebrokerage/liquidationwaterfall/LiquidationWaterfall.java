package com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool;
import com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallV2;
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

public final class LiquidationWaterfall extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.PrimeBrokerage.LiquidationWaterfall", "LiquidationWaterfall");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18", "Canton.ETF.PrimeBrokerage.LiquidationWaterfall", "LiquidationWaterfall");

  public static final String PACKAGE_ID = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<LiquidationWaterfall, ExecuteWaterfall, Tuple2<ContractId, CollateralPool.ContractId>> CHOICE_ExecuteWaterfall = 
      Choice.create("ExecuteWaterfall", value$ -> value$.toValue(), value$ ->
        ExecuteWaterfall.valueDecoder().decode(value$), value$ ->
        Tuple2.<com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationWaterfall.ContractId,
        com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool.ContractId>valueDecoder(v$0 ->
          new ContractId(v$0.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        v$1 ->
          new CollateralPool.ContractId(v$1.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()))
        .decode(value$), new ExecuteWaterfall.JsonDecoder$().get(),
        new Tuple2.JsonDecoder$().get(JsonLfDecoders.contractId(ContractId::new), JsonLfDecoders.contractId(CollateralPool.ContractId::new)),
        ExecuteWaterfall::jsonEncoder,
        _x0 -> _x0.jsonEncoder(JsonLfEncoders::contractId, JsonLfEncoders::contractId));

  public static final Choice<LiquidationWaterfall, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, LiquidationWaterfall> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(LiquidationWaterfall.PACKAGE_ID, LiquidationWaterfall.PACKAGE_NAME, LiquidationWaterfall.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationWaterfall",
        TEMPLATE_ID, ContractId::new, v -> LiquidationWaterfall.templateValueDecoder().decode(v),
        LiquidationWaterfall::fromJson, Contract::new, List.of(CHOICE_ExecuteWaterfall,
        CHOICE_Archive));

  public final String primeBroker;

  public final String hedgeFund;

  public final String riskManager;

  public final String waterfallId;

  public final MarginCallV2.ContractId marginCallCid;

  public final CollateralPool.ContractId poolCid;

  public final BigDecimal originalShortfall;

  public final BigDecimal remainingShortfall;

  public final List<LiquidationStep> liquidationSteps;

  public final WaterfallStatus status;

  public final Instant initiatedAt;

  public final Instant updatedAt;

  public LiquidationWaterfall(String primeBroker, String hedgeFund, String riskManager,
      String waterfallId, MarginCallV2.ContractId marginCallCid, CollateralPool.ContractId poolCid,
      BigDecimal originalShortfall, BigDecimal remainingShortfall,
      List<LiquidationStep> liquidationSteps, WaterfallStatus status, Instant initiatedAt,
      Instant updatedAt) {
    this.primeBroker = primeBroker;
    this.hedgeFund = hedgeFund;
    this.riskManager = riskManager;
    this.waterfallId = waterfallId;
    this.marginCallCid = marginCallCid;
    this.poolCid = poolCid;
    this.originalShortfall = originalShortfall;
    this.remainingShortfall = remainingShortfall;
    this.liquidationSteps = liquidationSteps;
    this.status = status;
    this.initiatedAt = initiatedAt;
    this.updatedAt = updatedAt;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(LiquidationWaterfall.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseExecuteWaterfall} instead
   */
  @Deprecated
  public Update<Exercised<Tuple2<ContractId, CollateralPool.ContractId>>> createAndExerciseExecuteWaterfall(
      ExecuteWaterfall arg) {
    return createAnd().exerciseExecuteWaterfall(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseExecuteWaterfall} instead
   */
  @Deprecated
  public Update<Exercised<Tuple2<ContractId, CollateralPool.ContractId>>> createAndExerciseExecuteWaterfall(
      List<LiquidationPriority> priorities) {
    return createAndExerciseExecuteWaterfall(new ExecuteWaterfall(priorities));
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

  public static Update<Created<ContractId>> create(String primeBroker, String hedgeFund,
      String riskManager, String waterfallId, MarginCallV2.ContractId marginCallCid,
      CollateralPool.ContractId poolCid, BigDecimal originalShortfall,
      BigDecimal remainingShortfall, List<LiquidationStep> liquidationSteps, WaterfallStatus status,
      Instant initiatedAt, Instant updatedAt) {
    return new LiquidationWaterfall(primeBroker, hedgeFund, riskManager, waterfallId, marginCallCid,
        poolCid, originalShortfall, remainingShortfall, liquidationSteps, status, initiatedAt,
        updatedAt).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, LiquidationWaterfall> getCompanion(
      ) {
    return COMPANION;
  }

  public static ValueDecoder<LiquidationWaterfall> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(12);
    fields.add(new DamlRecord.Field("primeBroker", new Party(this.primeBroker)));
    fields.add(new DamlRecord.Field("hedgeFund", new Party(this.hedgeFund)));
    fields.add(new DamlRecord.Field("riskManager", new Party(this.riskManager)));
    fields.add(new DamlRecord.Field("waterfallId", new Text(this.waterfallId)));
    fields.add(new DamlRecord.Field("marginCallCid", this.marginCallCid.toValue()));
    fields.add(new DamlRecord.Field("poolCid", this.poolCid.toValue()));
    fields.add(new DamlRecord.Field("originalShortfall", new Numeric(this.originalShortfall)));
    fields.add(new DamlRecord.Field("remainingShortfall", new Numeric(this.remainingShortfall)));
    fields.add(new DamlRecord.Field("liquidationSteps", this.liquidationSteps.stream().collect(DamlCollectors.toDamlList(v$0 -> v$0.toValue()))));
    fields.add(new DamlRecord.Field("status", this.status.toValue()));
    fields.add(new DamlRecord.Field("initiatedAt", Timestamp.fromInstant(this.initiatedAt)));
    fields.add(new DamlRecord.Field("updatedAt", Timestamp.fromInstant(this.updatedAt)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<LiquidationWaterfall> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(12,0, recordValue$);
      String primeBroker = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String hedgeFund = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String riskManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String waterfallId = PrimitiveValueDecoders.fromText.decode(fields$.get(3).getValue());
      MarginCallV2.ContractId marginCallCid =
          new MarginCallV2.ContractId(fields$.get(4).getValue().asContractId().orElseThrow(() -> new IllegalArgumentException("Expected marginCallCid to be of type com.daml.ledger.javaapi.data.ContractId")).getValue());
      CollateralPool.ContractId poolCid =
          new CollateralPool.ContractId(fields$.get(5).getValue().asContractId().orElseThrow(() -> new IllegalArgumentException("Expected poolCid to be of type com.daml.ledger.javaapi.data.ContractId")).getValue());
      BigDecimal originalShortfall = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(6).getValue());
      BigDecimal remainingShortfall = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(7).getValue());
      List<LiquidationStep> liquidationSteps = PrimitiveValueDecoders.fromList(
            LiquidationStep.valueDecoder()).decode(fields$.get(8).getValue());
      WaterfallStatus status = WaterfallStatus.valueDecoder().decode(fields$.get(9).getValue());
      Instant initiatedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(10).getValue());
      Instant updatedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(11).getValue());
      return new LiquidationWaterfall(primeBroker, hedgeFund, riskManager, waterfallId,
          marginCallCid, poolCid, originalShortfall, remainingShortfall, liquidationSteps, status,
          initiatedAt, updatedAt);
    } ;
  }

  public static JsonLfDecoder<LiquidationWaterfall> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("primeBroker", "hedgeFund", "riskManager", "waterfallId", "marginCallCid", "poolCid", "originalShortfall", "remainingShortfall", "liquidationSteps", "status", "initiatedAt", "updatedAt"), name -> {
          switch (name) {
            case "primeBroker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "hedgeFund": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "riskManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "waterfallId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "marginCallCid": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.contractId(com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallV2.ContractId::new));
            case "poolCid": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.contractId(com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool.ContractId::new));
            case "originalShortfall": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "remainingShortfall": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "liquidationSteps": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(8, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.list(new com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationStep.JsonDecoder$().get()));
            case "status": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(9, new com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.WaterfallStatus.JsonDecoder$().get());
            case "initiatedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(10, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            case "updatedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(11, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new LiquidationWaterfall(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7]), JsonLfDecoders.cast(args[8]), JsonLfDecoders.cast(args[9]), JsonLfDecoders.cast(args[10]), JsonLfDecoders.cast(args[11])));
  }

  public static LiquidationWaterfall fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("primeBroker", apply(JsonLfEncoders::party, primeBroker)),
        JsonLfEncoders.Field.of("hedgeFund", apply(JsonLfEncoders::party, hedgeFund)),
        JsonLfEncoders.Field.of("riskManager", apply(JsonLfEncoders::party, riskManager)),
        JsonLfEncoders.Field.of("waterfallId", apply(JsonLfEncoders::text, waterfallId)),
        JsonLfEncoders.Field.of("marginCallCid", apply(JsonLfEncoders::contractId, marginCallCid)),
        JsonLfEncoders.Field.of("poolCid", apply(JsonLfEncoders::contractId, poolCid)),
        JsonLfEncoders.Field.of("originalShortfall", apply(JsonLfEncoders::numeric, originalShortfall)),
        JsonLfEncoders.Field.of("remainingShortfall", apply(JsonLfEncoders::numeric, remainingShortfall)),
        JsonLfEncoders.Field.of("liquidationSteps", apply(JsonLfEncoders.list(LiquidationStep::jsonEncoder), liquidationSteps)),
        JsonLfEncoders.Field.of("status", apply(WaterfallStatus::jsonEncoder, status)),
        JsonLfEncoders.Field.of("initiatedAt", apply(JsonLfEncoders::timestamp, initiatedAt)),
        JsonLfEncoders.Field.of("updatedAt", apply(JsonLfEncoders::timestamp, updatedAt)));
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
    if (!(object instanceof LiquidationWaterfall)) {
      return false;
    }
    LiquidationWaterfall other = (LiquidationWaterfall) object;
    return Objects.equals(this.primeBroker, other.primeBroker) &&
        Objects.equals(this.hedgeFund, other.hedgeFund) &&
        Objects.equals(this.riskManager, other.riskManager) &&
        Objects.equals(this.waterfallId, other.waterfallId) &&
        Objects.equals(this.marginCallCid, other.marginCallCid) &&
        Objects.equals(this.poolCid, other.poolCid) &&
        Objects.equals(this.originalShortfall, other.originalShortfall) &&
        Objects.equals(this.remainingShortfall, other.remainingShortfall) &&
        Objects.equals(this.liquidationSteps, other.liquidationSteps) &&
        Objects.equals(this.status, other.status) &&
        Objects.equals(this.initiatedAt, other.initiatedAt) &&
        Objects.equals(this.updatedAt, other.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.primeBroker, this.hedgeFund, this.riskManager, this.waterfallId,
        this.marginCallCid, this.poolCid, this.originalShortfall, this.remainingShortfall,
        this.liquidationSteps, this.status, this.initiatedAt, this.updatedAt);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationWaterfall(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        this.primeBroker, this.hedgeFund, this.riskManager, this.waterfallId, this.marginCallCid,
        this.poolCid, this.originalShortfall, this.remainingShortfall, this.liquidationSteps,
        this.status, this.initiatedAt, this.updatedAt);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<LiquidationWaterfall> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, LiquidationWaterfall, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<LiquidationWaterfall> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, LiquidationWaterfall> {
    public Contract(ContractId id, LiquidationWaterfall data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, LiquidationWaterfall> getCompanion() {
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
    default Update<Exercised<Tuple2<ContractId, CollateralPool.ContractId>>> exerciseExecuteWaterfall(
        ExecuteWaterfall arg) {
      return makeExerciseCmd(CHOICE_ExecuteWaterfall, arg);
    }

    default Update<Exercised<Tuple2<ContractId, CollateralPool.ContractId>>> exerciseExecuteWaterfall(
        List<LiquidationPriority> priorities) {
      return exerciseExecuteWaterfall(new ExecuteWaterfall(priorities));
    }

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
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, LiquidationWaterfall, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<LiquidationWaterfall> get() {
      return jsonDecoder();
    }
  }
}
