package com.canton.etf.model.canton.etf.primebrokerage.margincallv2;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool;
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

public final class MarginCallV2 extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.PrimeBrokerage.MarginCallV2", "MarginCallV2");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18", "Canton.ETF.PrimeBrokerage.MarginCallV2", "MarginCallV2");

  public static final String PACKAGE_ID = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<MarginCallV2, SatisfyCall, ContractId> CHOICE_SatisfyCall = 
      Choice.create("SatisfyCall", value$ -> value$.toValue(), value$ -> SatisfyCall.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new SatisfyCall.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        SatisfyCall::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<MarginCallV2, RespondToCall, ContractId> CHOICE_RespondToCall = 
      Choice.create("RespondToCall", value$ -> value$.toValue(), value$ ->
        RespondToCall.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new RespondToCall.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        RespondToCall::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<MarginCallV2, UpdateCoverage, ContractId> CHOICE_UpdateCoverage = 
      Choice.create("UpdateCoverage", value$ -> value$.toValue(), value$ ->
        UpdateCoverage.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new UpdateCoverage.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        UpdateCoverage::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<MarginCallV2, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<MarginCallV2, DeclareDefault, ContractId> CHOICE_DeclareDefault = 
      Choice.create("DeclareDefault", value$ -> value$.toValue(), value$ ->
        DeclareDefault.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new DeclareDefault.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        DeclareDefault::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, MarginCallV2> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(MarginCallV2.PACKAGE_ID, MarginCallV2.PACKAGE_NAME, MarginCallV2.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallV2", TEMPLATE_ID,
        ContractId::new, v -> MarginCallV2.templateValueDecoder().decode(v), MarginCallV2::fromJson,
        Contract::new, List.of(CHOICE_SatisfyCall, CHOICE_RespondToCall, CHOICE_DeclareDefault,
        CHOICE_UpdateCoverage, CHOICE_Archive));

  public final String primeBroker;

  public final String hedgeFund;

  public final String riskManager;

  public final String callId;

  public final CollateralPool.ContractId poolCid;

  public final BigDecimal requiredCoverage;

  public final BigDecimal currentCoverage;

  public final BigDecimal shortfall;

  public final Instant responseDeadline;

  public final MarginCallStatus status;

  public final Instant issuedAt;

  public final Instant updatedAt;

  public MarginCallV2(String primeBroker, String hedgeFund, String riskManager, String callId,
      CollateralPool.ContractId poolCid, BigDecimal requiredCoverage, BigDecimal currentCoverage,
      BigDecimal shortfall, Instant responseDeadline, MarginCallStatus status, Instant issuedAt,
      Instant updatedAt) {
    this.primeBroker = primeBroker;
    this.hedgeFund = hedgeFund;
    this.riskManager = riskManager;
    this.callId = callId;
    this.poolCid = poolCid;
    this.requiredCoverage = requiredCoverage;
    this.currentCoverage = currentCoverage;
    this.shortfall = shortfall;
    this.responseDeadline = responseDeadline;
    this.status = status;
    this.issuedAt = issuedAt;
    this.updatedAt = updatedAt;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(MarginCallV2.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseSatisfyCall} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseSatisfyCall(SatisfyCall arg) {
    return createAnd().exerciseSatisfyCall(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseSatisfyCall} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseSatisfyCall(BigDecimal newCoverage) {
    return createAndExerciseSatisfyCall(new SatisfyCall(newCoverage));
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRespondToCall} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRespondToCall(RespondToCall arg) {
    return createAnd().exerciseRespondToCall(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRespondToCall} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRespondToCall(ResponseType responseType,
      String comment) {
    return createAndExerciseRespondToCall(new RespondToCall(responseType, comment));
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateCoverage} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateCoverage(UpdateCoverage arg) {
    return createAnd().exerciseUpdateCoverage(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateCoverage} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateCoverage(BigDecimal newCoverage) {
    return createAndExerciseUpdateCoverage(new UpdateCoverage(newCoverage));
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseDeclareDefault} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseDeclareDefault(DeclareDefault arg) {
    return createAnd().exerciseDeclareDefault(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseDeclareDefault} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseDeclareDefault() {
    return createAndExerciseDeclareDefault(new DeclareDefault());
  }

  public static Update<Created<ContractId>> create(String primeBroker, String hedgeFund,
      String riskManager, String callId, CollateralPool.ContractId poolCid,
      BigDecimal requiredCoverage, BigDecimal currentCoverage, BigDecimal shortfall,
      Instant responseDeadline, MarginCallStatus status, Instant issuedAt, Instant updatedAt) {
    return new MarginCallV2(primeBroker, hedgeFund, riskManager, callId, poolCid, requiredCoverage,
        currentCoverage, shortfall, responseDeadline, status, issuedAt, updatedAt).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, MarginCallV2> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<MarginCallV2> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(12);
    fields.add(new DamlRecord.Field("primeBroker", new Party(this.primeBroker)));
    fields.add(new DamlRecord.Field("hedgeFund", new Party(this.hedgeFund)));
    fields.add(new DamlRecord.Field("riskManager", new Party(this.riskManager)));
    fields.add(new DamlRecord.Field("callId", new Text(this.callId)));
    fields.add(new DamlRecord.Field("poolCid", this.poolCid.toValue()));
    fields.add(new DamlRecord.Field("requiredCoverage", new Numeric(this.requiredCoverage)));
    fields.add(new DamlRecord.Field("currentCoverage", new Numeric(this.currentCoverage)));
    fields.add(new DamlRecord.Field("shortfall", new Numeric(this.shortfall)));
    fields.add(new DamlRecord.Field("responseDeadline", Timestamp.fromInstant(this.responseDeadline)));
    fields.add(new DamlRecord.Field("status", this.status.toValue()));
    fields.add(new DamlRecord.Field("issuedAt", Timestamp.fromInstant(this.issuedAt)));
    fields.add(new DamlRecord.Field("updatedAt", Timestamp.fromInstant(this.updatedAt)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<MarginCallV2> templateValueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(12,0, recordValue$);
      String primeBroker = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String hedgeFund = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String riskManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String callId = PrimitiveValueDecoders.fromText.decode(fields$.get(3).getValue());
      CollateralPool.ContractId poolCid =
          new CollateralPool.ContractId(fields$.get(4).getValue().asContractId().orElseThrow(() -> new IllegalArgumentException("Expected poolCid to be of type com.daml.ledger.javaapi.data.ContractId")).getValue());
      BigDecimal requiredCoverage = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(5).getValue());
      BigDecimal currentCoverage = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(6).getValue());
      BigDecimal shortfall = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(7).getValue());
      Instant responseDeadline = PrimitiveValueDecoders.fromTimestamp
          .decode(fields$.get(8).getValue());
      MarginCallStatus status = MarginCallStatus.valueDecoder().decode(fields$.get(9).getValue());
      Instant issuedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(10).getValue());
      Instant updatedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(11).getValue());
      return new MarginCallV2(primeBroker, hedgeFund, riskManager, callId, poolCid,
          requiredCoverage, currentCoverage, shortfall, responseDeadline, status, issuedAt,
          updatedAt);
    } ;
  }

  public static JsonLfDecoder<MarginCallV2> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("primeBroker", "hedgeFund", "riskManager", "callId", "poolCid", "requiredCoverage", "currentCoverage", "shortfall", "responseDeadline", "status", "issuedAt", "updatedAt"), name -> {
          switch (name) {
            case "primeBroker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "hedgeFund": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "riskManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "callId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "poolCid": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.contractId(com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool.ContractId::new));
            case "requiredCoverage": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "currentCoverage": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "shortfall": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "responseDeadline": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(8, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            case "status": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(9, new com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallStatus.JsonDecoder$().get());
            case "issuedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(10, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            case "updatedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(11, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new MarginCallV2(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7]), JsonLfDecoders.cast(args[8]), JsonLfDecoders.cast(args[9]), JsonLfDecoders.cast(args[10]), JsonLfDecoders.cast(args[11])));
  }

  public static MarginCallV2 fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("primeBroker", apply(JsonLfEncoders::party, primeBroker)),
        JsonLfEncoders.Field.of("hedgeFund", apply(JsonLfEncoders::party, hedgeFund)),
        JsonLfEncoders.Field.of("riskManager", apply(JsonLfEncoders::party, riskManager)),
        JsonLfEncoders.Field.of("callId", apply(JsonLfEncoders::text, callId)),
        JsonLfEncoders.Field.of("poolCid", apply(JsonLfEncoders::contractId, poolCid)),
        JsonLfEncoders.Field.of("requiredCoverage", apply(JsonLfEncoders::numeric, requiredCoverage)),
        JsonLfEncoders.Field.of("currentCoverage", apply(JsonLfEncoders::numeric, currentCoverage)),
        JsonLfEncoders.Field.of("shortfall", apply(JsonLfEncoders::numeric, shortfall)),
        JsonLfEncoders.Field.of("responseDeadline", apply(JsonLfEncoders::timestamp, responseDeadline)),
        JsonLfEncoders.Field.of("status", apply(MarginCallStatus::jsonEncoder, status)),
        JsonLfEncoders.Field.of("issuedAt", apply(JsonLfEncoders::timestamp, issuedAt)),
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
    if (!(object instanceof MarginCallV2)) {
      return false;
    }
    MarginCallV2 other = (MarginCallV2) object;
    return Objects.equals(this.primeBroker, other.primeBroker) &&
        Objects.equals(this.hedgeFund, other.hedgeFund) &&
        Objects.equals(this.riskManager, other.riskManager) &&
        Objects.equals(this.callId, other.callId) && Objects.equals(this.poolCid, other.poolCid) &&
        Objects.equals(this.requiredCoverage, other.requiredCoverage) &&
        Objects.equals(this.currentCoverage, other.currentCoverage) &&
        Objects.equals(this.shortfall, other.shortfall) &&
        Objects.equals(this.responseDeadline, other.responseDeadline) &&
        Objects.equals(this.status, other.status) &&
        Objects.equals(this.issuedAt, other.issuedAt) &&
        Objects.equals(this.updatedAt, other.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.primeBroker, this.hedgeFund, this.riskManager, this.callId,
        this.poolCid, this.requiredCoverage, this.currentCoverage, this.shortfall,
        this.responseDeadline, this.status, this.issuedAt, this.updatedAt);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.margincallv2.MarginCallV2(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        this.primeBroker, this.hedgeFund, this.riskManager, this.callId, this.poolCid,
        this.requiredCoverage, this.currentCoverage, this.shortfall, this.responseDeadline,
        this.status, this.issuedAt, this.updatedAt);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<MarginCallV2> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, MarginCallV2, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<MarginCallV2> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, MarginCallV2> {
    public Contract(ContractId id, MarginCallV2 data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, MarginCallV2> getCompanion() {
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
    default Update<Exercised<ContractId>> exerciseSatisfyCall(SatisfyCall arg) {
      return makeExerciseCmd(CHOICE_SatisfyCall, arg);
    }

    default Update<Exercised<ContractId>> exerciseSatisfyCall(BigDecimal newCoverage) {
      return exerciseSatisfyCall(new SatisfyCall(newCoverage));
    }

    default Update<Exercised<ContractId>> exerciseRespondToCall(RespondToCall arg) {
      return makeExerciseCmd(CHOICE_RespondToCall, arg);
    }

    default Update<Exercised<ContractId>> exerciseRespondToCall(ResponseType responseType,
        String comment) {
      return exerciseRespondToCall(new RespondToCall(responseType, comment));
    }

    default Update<Exercised<ContractId>> exerciseUpdateCoverage(UpdateCoverage arg) {
      return makeExerciseCmd(CHOICE_UpdateCoverage, arg);
    }

    default Update<Exercised<ContractId>> exerciseUpdateCoverage(BigDecimal newCoverage) {
      return exerciseUpdateCoverage(new UpdateCoverage(newCoverage));
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<ContractId>> exerciseDeclareDefault(DeclareDefault arg) {
      return makeExerciseCmd(CHOICE_DeclareDefault, arg);
    }

    default Update<Exercised<ContractId>> exerciseDeclareDefault() {
      return exerciseDeclareDefault(new DeclareDefault());
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, MarginCallV2, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<MarginCallV2> get() {
      return jsonDecoder();
    }
  }
}
