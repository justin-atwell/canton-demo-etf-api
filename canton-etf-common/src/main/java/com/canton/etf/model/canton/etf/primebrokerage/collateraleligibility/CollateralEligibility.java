package com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition;
import com.canton.etf.model.da.internal.template.Archive;
import com.daml.ledger.javaapi.data.Bool;
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
import java.lang.Boolean;
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

public final class CollateralEligibility extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.PrimeBrokerage.CollateralEligibility", "CollateralEligibility");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2", "Canton.ETF.PrimeBrokerage.CollateralEligibility", "CollateralEligibility");

  public static final String PACKAGE_ID = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<CollateralEligibility, CheckEligibility, EligibilityResult> CHOICE_CheckEligibility = 
      Choice.create("CheckEligibility", value$ -> value$.toValue(), value$ ->
        CheckEligibility.valueDecoder().decode(value$), value$ -> EligibilityResult.valueDecoder()
        .decode(value$), new CheckEligibility.JsonDecoder$().get(),
        new EligibilityResult.JsonDecoder$().get(), CheckEligibility::jsonEncoder,
        EligibilityResult::jsonEncoder);

  public static final Choice<CollateralEligibility, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<CollateralEligibility, UpdateSchedule, ContractId> CHOICE_UpdateSchedule = 
      Choice.create("UpdateSchedule", value$ -> value$.toValue(), value$ ->
        UpdateSchedule.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new UpdateSchedule.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        UpdateSchedule::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, CollateralEligibility> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(CollateralEligibility.PACKAGE_ID, CollateralEligibility.PACKAGE_NAME, CollateralEligibility.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.CollateralEligibility",
        TEMPLATE_ID, ContractId::new, v -> CollateralEligibility.templateValueDecoder().decode(v),
        CollateralEligibility::fromJson, Contract::new, List.of(CHOICE_CheckEligibility,
        CHOICE_Archive, CHOICE_UpdateSchedule));

  public final String primeBroker;

  public final String hedgeFund;

  public final String scheduleId;

  public final Boolean acceptsTreasuries;

  public final Boolean acceptsBitcoin;

  public final Boolean acceptsMMF;

  public final BigDecimal maxBtcConcentration;

  public final BigDecimal maxTreasuryConc;

  public final BigDecimal maxMmfConcentration;

  public final BigDecimal minHaircutAdjValue;

  public CollateralEligibility(String primeBroker, String hedgeFund, String scheduleId,
      Boolean acceptsTreasuries, Boolean acceptsBitcoin, Boolean acceptsMMF,
      BigDecimal maxBtcConcentration, BigDecimal maxTreasuryConc, BigDecimal maxMmfConcentration,
      BigDecimal minHaircutAdjValue) {
    this.primeBroker = primeBroker;
    this.hedgeFund = hedgeFund;
    this.scheduleId = scheduleId;
    this.acceptsTreasuries = acceptsTreasuries;
    this.acceptsBitcoin = acceptsBitcoin;
    this.acceptsMMF = acceptsMMF;
    this.maxBtcConcentration = maxBtcConcentration;
    this.maxTreasuryConc = maxTreasuryConc;
    this.maxMmfConcentration = maxMmfConcentration;
    this.minHaircutAdjValue = minHaircutAdjValue;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(CollateralEligibility.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseCheckEligibility} instead
   */
  @Deprecated
  public Update<Exercised<EligibilityResult>> createAndExerciseCheckEligibility(
      CheckEligibility arg) {
    return createAnd().exerciseCheckEligibility(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseCheckEligibility} instead
   */
  @Deprecated
  public Update<Exercised<EligibilityResult>> createAndExerciseCheckEligibility(
      CollateralPosition position) {
    return createAndExerciseCheckEligibility(new CheckEligibility(position));
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateSchedule} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateSchedule(UpdateSchedule arg) {
    return createAnd().exerciseUpdateSchedule(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateSchedule} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateSchedule(BigDecimal newMaxBtcConc,
      BigDecimal newMaxTreasuryConc, BigDecimal newMaxMmfConc, BigDecimal newMinValue) {
    return createAndExerciseUpdateSchedule(new UpdateSchedule(newMaxBtcConc, newMaxTreasuryConc,
        newMaxMmfConc, newMinValue));
  }

  public static Update<Created<ContractId>> create(String primeBroker, String hedgeFund,
      String scheduleId, Boolean acceptsTreasuries, Boolean acceptsBitcoin, Boolean acceptsMMF,
      BigDecimal maxBtcConcentration, BigDecimal maxTreasuryConc, BigDecimal maxMmfConcentration,
      BigDecimal minHaircutAdjValue) {
    return new CollateralEligibility(primeBroker, hedgeFund, scheduleId, acceptsTreasuries,
        acceptsBitcoin, acceptsMMF, maxBtcConcentration, maxTreasuryConc, maxMmfConcentration,
        minHaircutAdjValue).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, CollateralEligibility> getCompanion(
      ) {
    return COMPANION;
  }

  public static ValueDecoder<CollateralEligibility> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(10);
    fields.add(new DamlRecord.Field("primeBroker", new Party(this.primeBroker)));
    fields.add(new DamlRecord.Field("hedgeFund", new Party(this.hedgeFund)));
    fields.add(new DamlRecord.Field("scheduleId", new Text(this.scheduleId)));
    fields.add(new DamlRecord.Field("acceptsTreasuries", Bool.of(this.acceptsTreasuries)));
    fields.add(new DamlRecord.Field("acceptsBitcoin", Bool.of(this.acceptsBitcoin)));
    fields.add(new DamlRecord.Field("acceptsMMF", Bool.of(this.acceptsMMF)));
    fields.add(new DamlRecord.Field("maxBtcConcentration", new Numeric(this.maxBtcConcentration)));
    fields.add(new DamlRecord.Field("maxTreasuryConc", new Numeric(this.maxTreasuryConc)));
    fields.add(new DamlRecord.Field("maxMmfConcentration", new Numeric(this.maxMmfConcentration)));
    fields.add(new DamlRecord.Field("minHaircutAdjValue", new Numeric(this.minHaircutAdjValue)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<CollateralEligibility> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(10,0, recordValue$);
      String primeBroker = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String hedgeFund = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String scheduleId = PrimitiveValueDecoders.fromText.decode(fields$.get(2).getValue());
      Boolean acceptsTreasuries = PrimitiveValueDecoders.fromBool.decode(fields$.get(3).getValue());
      Boolean acceptsBitcoin = PrimitiveValueDecoders.fromBool.decode(fields$.get(4).getValue());
      Boolean acceptsMMF = PrimitiveValueDecoders.fromBool.decode(fields$.get(5).getValue());
      BigDecimal maxBtcConcentration = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(6).getValue());
      BigDecimal maxTreasuryConc = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(7).getValue());
      BigDecimal maxMmfConcentration = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(8).getValue());
      BigDecimal minHaircutAdjValue = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(9).getValue());
      return new CollateralEligibility(primeBroker, hedgeFund, scheduleId, acceptsTreasuries,
          acceptsBitcoin, acceptsMMF, maxBtcConcentration, maxTreasuryConc, maxMmfConcentration,
          minHaircutAdjValue);
    } ;
  }

  public static JsonLfDecoder<CollateralEligibility> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("primeBroker", "hedgeFund", "scheduleId", "acceptsTreasuries", "acceptsBitcoin", "acceptsMMF", "maxBtcConcentration", "maxTreasuryConc", "maxMmfConcentration", "minHaircutAdjValue"), name -> {
          switch (name) {
            case "primeBroker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "hedgeFund": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "scheduleId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "acceptsTreasuries": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.bool);
            case "acceptsBitcoin": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.bool);
            case "acceptsMMF": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.bool);
            case "maxBtcConcentration": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "maxTreasuryConc": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "maxMmfConcentration": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(8, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "minHaircutAdjValue": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(9, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            default: return null;
          }
        }
        , (Object[] args) -> new CollateralEligibility(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7]), JsonLfDecoders.cast(args[8]), JsonLfDecoders.cast(args[9])));
  }

  public static CollateralEligibility fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("primeBroker", apply(JsonLfEncoders::party, primeBroker)),
        JsonLfEncoders.Field.of("hedgeFund", apply(JsonLfEncoders::party, hedgeFund)),
        JsonLfEncoders.Field.of("scheduleId", apply(JsonLfEncoders::text, scheduleId)),
        JsonLfEncoders.Field.of("acceptsTreasuries", apply(JsonLfEncoders::bool, acceptsTreasuries)),
        JsonLfEncoders.Field.of("acceptsBitcoin", apply(JsonLfEncoders::bool, acceptsBitcoin)),
        JsonLfEncoders.Field.of("acceptsMMF", apply(JsonLfEncoders::bool, acceptsMMF)),
        JsonLfEncoders.Field.of("maxBtcConcentration", apply(JsonLfEncoders::numeric, maxBtcConcentration)),
        JsonLfEncoders.Field.of("maxTreasuryConc", apply(JsonLfEncoders::numeric, maxTreasuryConc)),
        JsonLfEncoders.Field.of("maxMmfConcentration", apply(JsonLfEncoders::numeric, maxMmfConcentration)),
        JsonLfEncoders.Field.of("minHaircutAdjValue", apply(JsonLfEncoders::numeric, minHaircutAdjValue)));
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
    if (!(object instanceof CollateralEligibility)) {
      return false;
    }
    CollateralEligibility other = (CollateralEligibility) object;
    return Objects.equals(this.primeBroker, other.primeBroker) &&
        Objects.equals(this.hedgeFund, other.hedgeFund) &&
        Objects.equals(this.scheduleId, other.scheduleId) &&
        Objects.equals(this.acceptsTreasuries, other.acceptsTreasuries) &&
        Objects.equals(this.acceptsBitcoin, other.acceptsBitcoin) &&
        Objects.equals(this.acceptsMMF, other.acceptsMMF) &&
        Objects.equals(this.maxBtcConcentration, other.maxBtcConcentration) &&
        Objects.equals(this.maxTreasuryConc, other.maxTreasuryConc) &&
        Objects.equals(this.maxMmfConcentration, other.maxMmfConcentration) &&
        Objects.equals(this.minHaircutAdjValue, other.minHaircutAdjValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.primeBroker, this.hedgeFund, this.scheduleId, this.acceptsTreasuries,
        this.acceptsBitcoin, this.acceptsMMF, this.maxBtcConcentration, this.maxTreasuryConc,
        this.maxMmfConcentration, this.minHaircutAdjValue);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.collateraleligibility.CollateralEligibility(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        this.primeBroker, this.hedgeFund, this.scheduleId, this.acceptsTreasuries,
        this.acceptsBitcoin, this.acceptsMMF, this.maxBtcConcentration, this.maxTreasuryConc,
        this.maxMmfConcentration, this.minHaircutAdjValue);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<CollateralEligibility> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CollateralEligibility, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<CollateralEligibility> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, CollateralEligibility> {
    public Contract(ContractId id, CollateralEligibility data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, CollateralEligibility> getCompanion() {
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
    default Update<Exercised<EligibilityResult>> exerciseCheckEligibility(CheckEligibility arg) {
      return makeExerciseCmd(CHOICE_CheckEligibility, arg);
    }

    default Update<Exercised<EligibilityResult>> exerciseCheckEligibility(
        CollateralPosition position) {
      return exerciseCheckEligibility(new CheckEligibility(position));
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<ContractId>> exerciseUpdateSchedule(UpdateSchedule arg) {
      return makeExerciseCmd(CHOICE_UpdateSchedule, arg);
    }

    default Update<Exercised<ContractId>> exerciseUpdateSchedule(BigDecimal newMaxBtcConc,
        BigDecimal newMaxTreasuryConc, BigDecimal newMaxMmfConc, BigDecimal newMinValue) {
      return exerciseUpdateSchedule(new UpdateSchedule(newMaxBtcConc, newMaxTreasuryConc,
          newMaxMmfConc, newMinValue));
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CollateralEligibility, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<CollateralEligibility> get() {
      return jsonDecoder();
    }
  }
}
