package com.canton.etf.model.canton.etf.primebrokerage.collateralpool;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition;
import com.canton.etf.model.da.internal.template.Archive;
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

public final class CollateralPool extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.PrimeBrokerage.CollateralPool", "CollateralPool");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18", "Canton.ETF.PrimeBrokerage.CollateralPool", "CollateralPool");

  public static final String PACKAGE_ID = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<CollateralPool, AddPosition, ContractId> CHOICE_AddPosition = 
      Choice.create("AddPosition", value$ -> value$.toValue(), value$ -> AddPosition.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new AddPosition.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        AddPosition::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<CollateralPool, RemovePosition, ContractId> CHOICE_RemovePosition = 
      Choice.create("RemovePosition", value$ -> value$.toValue(), value$ ->
        RemovePosition.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new RemovePosition.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        RemovePosition::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<CollateralPool, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<CollateralPool, RevaluePool, ContractId> CHOICE_RevaluePool = 
      Choice.create("RevaluePool", value$ -> value$.toValue(), value$ -> RevaluePool.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new RevaluePool.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        RevaluePool::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, CollateralPool> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(CollateralPool.PACKAGE_ID, CollateralPool.PACKAGE_NAME, CollateralPool.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool", TEMPLATE_ID,
        ContractId::new, v -> CollateralPool.templateValueDecoder().decode(v),
        CollateralPool::fromJson, Contract::new, List.of(CHOICE_AddPosition, CHOICE_RemovePosition,
        CHOICE_Archive, CHOICE_RevaluePool));

  public final String hedgeFund;

  public final String custodian;

  public final String riskManager;

  public final String poolId;

  public final List<CollateralPosition> positions;

  public final BigDecimal totalValue;

  public final Instant lastUpdated;

  public CollateralPool(String hedgeFund, String custodian, String riskManager, String poolId,
      List<CollateralPosition> positions, BigDecimal totalValue, Instant lastUpdated) {
    this.hedgeFund = hedgeFund;
    this.custodian = custodian;
    this.riskManager = riskManager;
    this.poolId = poolId;
    this.positions = positions;
    this.totalValue = totalValue;
    this.lastUpdated = lastUpdated;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(CollateralPool.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseAddPosition} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseAddPosition(AddPosition arg) {
    return createAnd().exerciseAddPosition(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseAddPosition} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseAddPosition(
      CollateralPosition newPosition) {
    return createAndExerciseAddPosition(new AddPosition(newPosition));
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRemovePosition} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRemovePosition(RemovePosition arg) {
    return createAnd().exerciseRemovePosition(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRemovePosition} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRemovePosition(String positionId) {
    return createAndExerciseRemovePosition(new RemovePosition(positionId));
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRevaluePool} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRevaluePool(RevaluePool arg) {
    return createAnd().exerciseRevaluePool(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRevaluePool} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRevaluePool(
      List<CollateralPosition> updatedPositions) {
    return createAndExerciseRevaluePool(new RevaluePool(updatedPositions));
  }

  public static Update<Created<ContractId>> create(String hedgeFund, String custodian,
      String riskManager, String poolId, List<CollateralPosition> positions, BigDecimal totalValue,
      Instant lastUpdated) {
    return new CollateralPool(hedgeFund, custodian, riskManager, poolId, positions, totalValue,
        lastUpdated).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, CollateralPool> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<CollateralPool> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(7);
    fields.add(new DamlRecord.Field("hedgeFund", new Party(this.hedgeFund)));
    fields.add(new DamlRecord.Field("custodian", new Party(this.custodian)));
    fields.add(new DamlRecord.Field("riskManager", new Party(this.riskManager)));
    fields.add(new DamlRecord.Field("poolId", new Text(this.poolId)));
    fields.add(new DamlRecord.Field("positions", this.positions.stream().collect(DamlCollectors.toDamlList(v$0 -> v$0.toValue()))));
    fields.add(new DamlRecord.Field("totalValue", new Numeric(this.totalValue)));
    fields.add(new DamlRecord.Field("lastUpdated", Timestamp.fromInstant(this.lastUpdated)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<CollateralPool> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(7,0, recordValue$);
      String hedgeFund = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String custodian = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String riskManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String poolId = PrimitiveValueDecoders.fromText.decode(fields$.get(3).getValue());
      List<CollateralPosition> positions = PrimitiveValueDecoders.fromList(
            CollateralPosition.valueDecoder()).decode(fields$.get(4).getValue());
      BigDecimal totalValue = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(5).getValue());
      Instant lastUpdated = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(6).getValue());
      return new CollateralPool(hedgeFund, custodian, riskManager, poolId, positions, totalValue,
          lastUpdated);
    } ;
  }

  public static JsonLfDecoder<CollateralPool> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("hedgeFund", "custodian", "riskManager", "poolId", "positions", "totalValue", "lastUpdated"), name -> {
          switch (name) {
            case "hedgeFund": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "custodian": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "riskManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "poolId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "positions": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.list(new com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition.JsonDecoder$().get()));
            case "totalValue": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "lastUpdated": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new CollateralPool(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6])));
  }

  public static CollateralPool fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("hedgeFund", apply(JsonLfEncoders::party, hedgeFund)),
        JsonLfEncoders.Field.of("custodian", apply(JsonLfEncoders::party, custodian)),
        JsonLfEncoders.Field.of("riskManager", apply(JsonLfEncoders::party, riskManager)),
        JsonLfEncoders.Field.of("poolId", apply(JsonLfEncoders::text, poolId)),
        JsonLfEncoders.Field.of("positions", apply(JsonLfEncoders.list(CollateralPosition::jsonEncoder), positions)),
        JsonLfEncoders.Field.of("totalValue", apply(JsonLfEncoders::numeric, totalValue)),
        JsonLfEncoders.Field.of("lastUpdated", apply(JsonLfEncoders::timestamp, lastUpdated)));
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
    if (!(object instanceof CollateralPool)) {
      return false;
    }
    CollateralPool other = (CollateralPool) object;
    return Objects.equals(this.hedgeFund, other.hedgeFund) &&
        Objects.equals(this.custodian, other.custodian) &&
        Objects.equals(this.riskManager, other.riskManager) &&
        Objects.equals(this.poolId, other.poolId) &&
        Objects.equals(this.positions, other.positions) &&
        Objects.equals(this.totalValue, other.totalValue) &&
        Objects.equals(this.lastUpdated, other.lastUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.hedgeFund, this.custodian, this.riskManager, this.poolId,
        this.positions, this.totalValue, this.lastUpdated);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool(%s, %s, %s, %s, %s, %s, %s)",
        this.hedgeFund, this.custodian, this.riskManager, this.poolId, this.positions,
        this.totalValue, this.lastUpdated);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<CollateralPool> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CollateralPool, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<CollateralPool> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, CollateralPool> {
    public Contract(ContractId id, CollateralPool data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, CollateralPool> getCompanion() {
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
    default Update<Exercised<ContractId>> exerciseAddPosition(AddPosition arg) {
      return makeExerciseCmd(CHOICE_AddPosition, arg);
    }

    default Update<Exercised<ContractId>> exerciseAddPosition(CollateralPosition newPosition) {
      return exerciseAddPosition(new AddPosition(newPosition));
    }

    default Update<Exercised<ContractId>> exerciseRemovePosition(RemovePosition arg) {
      return makeExerciseCmd(CHOICE_RemovePosition, arg);
    }

    default Update<Exercised<ContractId>> exerciseRemovePosition(String positionId) {
      return exerciseRemovePosition(new RemovePosition(positionId));
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<ContractId>> exerciseRevaluePool(RevaluePool arg) {
      return makeExerciseCmd(CHOICE_RevaluePool, arg);
    }

    default Update<Exercised<ContractId>> exerciseRevaluePool(
        List<CollateralPosition> updatedPositions) {
      return exerciseRevaluePool(new RevaluePool(updatedPositions));
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CollateralPool, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<CollateralPool> get() {
      return jsonDecoder();
    }
  }
}
