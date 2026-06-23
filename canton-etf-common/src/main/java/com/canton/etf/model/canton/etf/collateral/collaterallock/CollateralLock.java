package com.canton.etf.model.canton.etf.collateral.collaterallock;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.collateral.collateralrelease.CollateralRelease;
import com.canton.etf.model.canton.etf.collateral.liquidationorder.LiquidationOrder;
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

public final class CollateralLock extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Collateral.CollateralLock", "CollateralLock");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2", "Canton.ETF.Collateral.CollateralLock", "CollateralLock");

  public static final String PACKAGE_ID = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<CollateralLock, Release, CollateralRelease.ContractId> CHOICE_Release = 
      Choice.create("Release", value$ -> value$.toValue(), value$ -> Release.valueDecoder()
        .decode(value$), value$ ->
        new CollateralRelease.ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new Release.JsonDecoder$().get(),
        JsonLfDecoders.contractId(CollateralRelease.ContractId::new), Release::jsonEncoder,
        JsonLfEncoders::contractId);

  public static final Choice<CollateralLock, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<CollateralLock, Liquidate, LiquidationOrder.ContractId> CHOICE_Liquidate = 
      Choice.create("Liquidate", value$ -> value$.toValue(), value$ -> Liquidate.valueDecoder()
        .decode(value$), value$ ->
        new LiquidationOrder.ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new Liquidate.JsonDecoder$().get(),
        JsonLfDecoders.contractId(LiquidationOrder.ContractId::new), Liquidate::jsonEncoder,
        JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, CollateralLock> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(CollateralLock.PACKAGE_ID, CollateralLock.PACKAGE_NAME, CollateralLock.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.collateral.collaterallock.CollateralLock", TEMPLATE_ID,
        ContractId::new, v -> CollateralLock.templateValueDecoder().decode(v),
        CollateralLock::fromJson, Contract::new, List.of(CHOICE_Release, CHOICE_Archive,
        CHOICE_Liquidate));

  public final String custodian;

  public final String fundManager;

  public final String compliance;

  public final String auditor;

  public final String asset;

  public final BigDecimal amount;

  public final String reason;

  public final Instant expiry;

  public final Instant lockedAt;

  public CollateralLock(String custodian, String fundManager, String compliance, String auditor,
      String asset, BigDecimal amount, String reason, Instant expiry, Instant lockedAt) {
    this.custodian = custodian;
    this.fundManager = fundManager;
    this.compliance = compliance;
    this.auditor = auditor;
    this.asset = asset;
    this.amount = amount;
    this.reason = reason;
    this.expiry = expiry;
    this.lockedAt = lockedAt;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(CollateralLock.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRelease} instead
   */
  @Deprecated
  public Update<Exercised<CollateralRelease.ContractId>> createAndExerciseRelease(Release arg) {
    return createAnd().exerciseRelease(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRelease} instead
   */
  @Deprecated
  public Update<Exercised<CollateralRelease.ContractId>> createAndExerciseRelease(
      String releaseReason) {
    return createAndExerciseRelease(new Release(releaseReason));
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseLiquidate} instead
   */
  @Deprecated
  public Update<Exercised<LiquidationOrder.ContractId>> createAndExerciseLiquidate(Liquidate arg) {
    return createAnd().exerciseLiquidate(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseLiquidate} instead
   */
  @Deprecated
  public Update<Exercised<LiquidationOrder.ContractId>> createAndExerciseLiquidate(
      String liquidateReason) {
    return createAndExerciseLiquidate(new Liquidate(liquidateReason));
  }

  public static Update<Created<ContractId>> create(String custodian, String fundManager,
      String compliance, String auditor, String asset, BigDecimal amount, String reason,
      Instant expiry, Instant lockedAt) {
    return new CollateralLock(custodian, fundManager, compliance, auditor, asset, amount, reason,
        expiry, lockedAt).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, CollateralLock> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<CollateralLock> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(9);
    fields.add(new DamlRecord.Field("custodian", new Party(this.custodian)));
    fields.add(new DamlRecord.Field("fundManager", new Party(this.fundManager)));
    fields.add(new DamlRecord.Field("compliance", new Party(this.compliance)));
    fields.add(new DamlRecord.Field("auditor", new Party(this.auditor)));
    fields.add(new DamlRecord.Field("asset", new Text(this.asset)));
    fields.add(new DamlRecord.Field("amount", new Numeric(this.amount)));
    fields.add(new DamlRecord.Field("reason", new Text(this.reason)));
    fields.add(new DamlRecord.Field("expiry", Timestamp.fromInstant(this.expiry)));
    fields.add(new DamlRecord.Field("lockedAt", Timestamp.fromInstant(this.lockedAt)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<CollateralLock> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(9,0, recordValue$);
      String custodian = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String fundManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String compliance = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String auditor = PrimitiveValueDecoders.fromParty.decode(fields$.get(3).getValue());
      String asset = PrimitiveValueDecoders.fromText.decode(fields$.get(4).getValue());
      BigDecimal amount = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(5).getValue());
      String reason = PrimitiveValueDecoders.fromText.decode(fields$.get(6).getValue());
      Instant expiry = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(7).getValue());
      Instant lockedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(8).getValue());
      return new CollateralLock(custodian, fundManager, compliance, auditor, asset, amount, reason,
          expiry, lockedAt);
    } ;
  }

  public static JsonLfDecoder<CollateralLock> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("custodian", "fundManager", "compliance", "auditor", "asset", "amount", "reason", "expiry", "lockedAt"), name -> {
          switch (name) {
            case "custodian": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "fundManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "compliance": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "auditor": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "asset": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "amount": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "reason": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "expiry": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            case "lockedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(8, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new CollateralLock(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7]), JsonLfDecoders.cast(args[8])));
  }

  public static CollateralLock fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("custodian", apply(JsonLfEncoders::party, custodian)),
        JsonLfEncoders.Field.of("fundManager", apply(JsonLfEncoders::party, fundManager)),
        JsonLfEncoders.Field.of("compliance", apply(JsonLfEncoders::party, compliance)),
        JsonLfEncoders.Field.of("auditor", apply(JsonLfEncoders::party, auditor)),
        JsonLfEncoders.Field.of("asset", apply(JsonLfEncoders::text, asset)),
        JsonLfEncoders.Field.of("amount", apply(JsonLfEncoders::numeric, amount)),
        JsonLfEncoders.Field.of("reason", apply(JsonLfEncoders::text, reason)),
        JsonLfEncoders.Field.of("expiry", apply(JsonLfEncoders::timestamp, expiry)),
        JsonLfEncoders.Field.of("lockedAt", apply(JsonLfEncoders::timestamp, lockedAt)));
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
    if (!(object instanceof CollateralLock)) {
      return false;
    }
    CollateralLock other = (CollateralLock) object;
    return Objects.equals(this.custodian, other.custodian) &&
        Objects.equals(this.fundManager, other.fundManager) &&
        Objects.equals(this.compliance, other.compliance) &&
        Objects.equals(this.auditor, other.auditor) && Objects.equals(this.asset, other.asset) &&
        Objects.equals(this.amount, other.amount) && Objects.equals(this.reason, other.reason) &&
        Objects.equals(this.expiry, other.expiry) && Objects.equals(this.lockedAt, other.lockedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.custodian, this.fundManager, this.compliance, this.auditor, this.asset,
        this.amount, this.reason, this.expiry, this.lockedAt);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.collateral.collaterallock.CollateralLock(%s, %s, %s, %s, %s, %s, %s, %s, %s)",
        this.custodian, this.fundManager, this.compliance, this.auditor, this.asset, this.amount,
        this.reason, this.expiry, this.lockedAt);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<CollateralLock> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CollateralLock, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<CollateralLock> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, CollateralLock> {
    public Contract(ContractId id, CollateralLock data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, CollateralLock> getCompanion() {
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
    default Update<Exercised<CollateralRelease.ContractId>> exerciseRelease(Release arg) {
      return makeExerciseCmd(CHOICE_Release, arg);
    }

    default Update<Exercised<CollateralRelease.ContractId>> exerciseRelease(String releaseReason) {
      return exerciseRelease(new Release(releaseReason));
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<LiquidationOrder.ContractId>> exerciseLiquidate(Liquidate arg) {
      return makeExerciseCmd(CHOICE_Liquidate, arg);
    }

    default Update<Exercised<LiquidationOrder.ContractId>> exerciseLiquidate(
        String liquidateReason) {
      return exerciseLiquidate(new Liquidate(liquidateReason));
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CollateralLock, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<CollateralLock> get() {
      return jsonDecoder();
    }
  }
}
