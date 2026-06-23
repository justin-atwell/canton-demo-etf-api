package com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.da.internal.template.Archive;
import com.daml.ledger.javaapi.data.ContractFilter;
import com.daml.ledger.javaapi.data.CreateAndExerciseCommand;
import com.daml.ledger.javaapi.data.CreateCommand;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.ExerciseCommand;
import com.daml.ledger.javaapi.data.Identifier;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class LiquidationAuditEvent extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.PrimeBrokerage.LiquidationWaterfall", "LiquidationAuditEvent");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18", "Canton.ETF.PrimeBrokerage.LiquidationWaterfall", "LiquidationAuditEvent");

  public static final String PACKAGE_ID = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<LiquidationAuditEvent, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, LiquidationAuditEvent> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(LiquidationAuditEvent.PACKAGE_ID, LiquidationAuditEvent.PACKAGE_NAME, LiquidationAuditEvent.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationAuditEvent",
        TEMPLATE_ID, ContractId::new, v -> LiquidationAuditEvent.templateValueDecoder().decode(v),
        LiquidationAuditEvent::fromJson, Contract::new, List.of(CHOICE_Archive));

  public final String primeBroker;

  public final String hedgeFund;

  public final String riskManager;

  public final String waterfallId;

  public final LiquidationStep step;

  public final Instant executedAt;

  public LiquidationAuditEvent(String primeBroker, String hedgeFund, String riskManager,
      String waterfallId, LiquidationStep step, Instant executedAt) {
    this.primeBroker = primeBroker;
    this.hedgeFund = hedgeFund;
    this.riskManager = riskManager;
    this.waterfallId = waterfallId;
    this.step = step;
    this.executedAt = executedAt;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(LiquidationAuditEvent.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
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
      String riskManager, String waterfallId, LiquidationStep step, Instant executedAt) {
    return new LiquidationAuditEvent(primeBroker, hedgeFund, riskManager, waterfallId, step,
        executedAt).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, LiquidationAuditEvent> getCompanion(
      ) {
    return COMPANION;
  }

  public static ValueDecoder<LiquidationAuditEvent> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(6);
    fields.add(new DamlRecord.Field("primeBroker", new Party(this.primeBroker)));
    fields.add(new DamlRecord.Field("hedgeFund", new Party(this.hedgeFund)));
    fields.add(new DamlRecord.Field("riskManager", new Party(this.riskManager)));
    fields.add(new DamlRecord.Field("waterfallId", new Text(this.waterfallId)));
    fields.add(new DamlRecord.Field("step", this.step.toValue()));
    fields.add(new DamlRecord.Field("executedAt", Timestamp.fromInstant(this.executedAt)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<LiquidationAuditEvent> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(6,0, recordValue$);
      String primeBroker = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String hedgeFund = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String riskManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String waterfallId = PrimitiveValueDecoders.fromText.decode(fields$.get(3).getValue());
      LiquidationStep step = LiquidationStep.valueDecoder().decode(fields$.get(4).getValue());
      Instant executedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(5).getValue());
      return new LiquidationAuditEvent(primeBroker, hedgeFund, riskManager, waterfallId, step,
          executedAt);
    } ;
  }

  public static JsonLfDecoder<LiquidationAuditEvent> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("primeBroker", "hedgeFund", "riskManager", "waterfallId", "step", "executedAt"), name -> {
          switch (name) {
            case "primeBroker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "hedgeFund": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "riskManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "waterfallId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "step": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, new com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationStep.JsonDecoder$().get());
            case "executedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new LiquidationAuditEvent(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5])));
  }

  public static LiquidationAuditEvent fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("primeBroker", apply(JsonLfEncoders::party, primeBroker)),
        JsonLfEncoders.Field.of("hedgeFund", apply(JsonLfEncoders::party, hedgeFund)),
        JsonLfEncoders.Field.of("riskManager", apply(JsonLfEncoders::party, riskManager)),
        JsonLfEncoders.Field.of("waterfallId", apply(JsonLfEncoders::text, waterfallId)),
        JsonLfEncoders.Field.of("step", apply(LiquidationStep::jsonEncoder, step)),
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
    if (!(object instanceof LiquidationAuditEvent)) {
      return false;
    }
    LiquidationAuditEvent other = (LiquidationAuditEvent) object;
    return Objects.equals(this.primeBroker, other.primeBroker) &&
        Objects.equals(this.hedgeFund, other.hedgeFund) &&
        Objects.equals(this.riskManager, other.riskManager) &&
        Objects.equals(this.waterfallId, other.waterfallId) &&
        Objects.equals(this.step, other.step) && Objects.equals(this.executedAt, other.executedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.primeBroker, this.hedgeFund, this.riskManager, this.waterfallId,
        this.step, this.executedAt);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.liquidationwaterfall.LiquidationAuditEvent(%s, %s, %s, %s, %s, %s)",
        this.primeBroker, this.hedgeFund, this.riskManager, this.waterfallId, this.step,
        this.executedAt);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<LiquidationAuditEvent> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, LiquidationAuditEvent, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<LiquidationAuditEvent> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, LiquidationAuditEvent> {
    public Contract(ContractId id, LiquidationAuditEvent data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, LiquidationAuditEvent> getCompanion() {
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
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, LiquidationAuditEvent, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<LiquidationAuditEvent> get() {
      return jsonDecoder();
    }
  }
}
