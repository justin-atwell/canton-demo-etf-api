package com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.AssetType;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.CollateralPosition;
import com.canton.etf.model.canton.etf.primebrokerage.collateralasset.HaircutRate;
import com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool;
import com.canton.etf.model.da.internal.template.Archive;
import com.canton.etf.model.da.types.Tuple2;
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

public final class SubstitutionRequest extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.PrimeBrokerage.SubstitutionRequest", "SubstitutionRequest");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2", "Canton.ETF.PrimeBrokerage.SubstitutionRequest", "SubstitutionRequest");

  public static final String PACKAGE_ID = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<SubstitutionRequest, ApproveByCustodian, ContractId> CHOICE_ApproveByCustodian = 
      Choice.create("ApproveByCustodian", value$ -> value$.toValue(), value$ ->
        ApproveByCustodian.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new ApproveByCustodian.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        ApproveByCustodian::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<SubstitutionRequest, ConfirmTransfer, Tuple2<ContractId, CollateralPool.ContractId>> CHOICE_ConfirmTransfer = 
      Choice.create("ConfirmTransfer", value$ -> value$.toValue(), value$ ->
        ConfirmTransfer.valueDecoder().decode(value$), value$ ->
        Tuple2.<com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionRequest.ContractId,
        com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool.ContractId>valueDecoder(v$0 ->
          new ContractId(v$0.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        v$1 ->
          new CollateralPool.ContractId(v$1.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()))
        .decode(value$), new ConfirmTransfer.JsonDecoder$().get(),
        new Tuple2.JsonDecoder$().get(JsonLfDecoders.contractId(ContractId::new), JsonLfDecoders.contractId(CollateralPool.ContractId::new)),
        ConfirmTransfer::jsonEncoder,
        _x0 -> _x0.jsonEncoder(JsonLfEncoders::contractId, JsonLfEncoders::contractId));

  public static final Choice<SubstitutionRequest, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<SubstitutionRequest, RejectByBroker, ContractId> CHOICE_RejectByBroker = 
      Choice.create("RejectByBroker", value$ -> value$.toValue(), value$ ->
        RejectByBroker.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new RejectByBroker.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        RejectByBroker::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<SubstitutionRequest, RejectByCustodian, ContractId> CHOICE_RejectByCustodian = 
      Choice.create("RejectByCustodian", value$ -> value$.toValue(), value$ ->
        RejectByCustodian.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new RejectByCustodian.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        RejectByCustodian::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, SubstitutionRequest> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(SubstitutionRequest.PACKAGE_ID, SubstitutionRequest.PACKAGE_NAME, SubstitutionRequest.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionRequest",
        TEMPLATE_ID, ContractId::new, v -> SubstitutionRequest.templateValueDecoder().decode(v),
        SubstitutionRequest::fromJson, Contract::new, List.of(CHOICE_RejectByCustodian,
        CHOICE_ApproveByCustodian, CHOICE_Archive, CHOICE_ConfirmTransfer, CHOICE_RejectByBroker));

  public final String hedgeFund;

  public final String primeBroker;

  public final String custodian;

  public final String riskManager;

  public final String requestId;

  public final String outgoingPosId;

  public final AssetType outgoingAsset;

  public final AssetType incomingAsset;

  public final HaircutRate incomingHaircut;

  public final CollateralPool.ContractId poolCid;

  public final SubstitutionStatus status;

  public final Instant createdAt;

  public final Instant updatedAt;

  public SubstitutionRequest(String hedgeFund, String primeBroker, String custodian,
      String riskManager, String requestId, String outgoingPosId, AssetType outgoingAsset,
      AssetType incomingAsset, HaircutRate incomingHaircut, CollateralPool.ContractId poolCid,
      SubstitutionStatus status, Instant createdAt, Instant updatedAt) {
    this.hedgeFund = hedgeFund;
    this.primeBroker = primeBroker;
    this.custodian = custodian;
    this.riskManager = riskManager;
    this.requestId = requestId;
    this.outgoingPosId = outgoingPosId;
    this.outgoingAsset = outgoingAsset;
    this.incomingAsset = incomingAsset;
    this.incomingHaircut = incomingHaircut;
    this.poolCid = poolCid;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(SubstitutionRequest.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseApproveByCustodian} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseApproveByCustodian(ApproveByCustodian arg) {
    return createAnd().exerciseApproveByCustodian(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseApproveByCustodian} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseApproveByCustodian(String comment) {
    return createAndExerciseApproveByCustodian(new ApproveByCustodian(comment));
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseConfirmTransfer} instead
   */
  @Deprecated
  public Update<Exercised<Tuple2<ContractId, CollateralPool.ContractId>>> createAndExerciseConfirmTransfer(
      ConfirmTransfer arg) {
    return createAnd().exerciseConfirmTransfer(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseConfirmTransfer} instead
   */
  @Deprecated
  public Update<Exercised<Tuple2<ContractId, CollateralPool.ContractId>>> createAndExerciseConfirmTransfer(
      CollateralPosition newIncomingPosition) {
    return createAndExerciseConfirmTransfer(new ConfirmTransfer(newIncomingPosition));
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRejectByBroker} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRejectByBroker(RejectByBroker arg) {
    return createAnd().exerciseRejectByBroker(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRejectByBroker} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRejectByBroker(String reason) {
    return createAndExerciseRejectByBroker(new RejectByBroker(reason));
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRejectByCustodian} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRejectByCustodian(RejectByCustodian arg) {
    return createAnd().exerciseRejectByCustodian(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRejectByCustodian} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseRejectByCustodian(String reason) {
    return createAndExerciseRejectByCustodian(new RejectByCustodian(reason));
  }

  public static Update<Created<ContractId>> create(String hedgeFund, String primeBroker,
      String custodian, String riskManager, String requestId, String outgoingPosId,
      AssetType outgoingAsset, AssetType incomingAsset, HaircutRate incomingHaircut,
      CollateralPool.ContractId poolCid, SubstitutionStatus status, Instant createdAt,
      Instant updatedAt) {
    return new SubstitutionRequest(hedgeFund, primeBroker, custodian, riskManager, requestId,
        outgoingPosId, outgoingAsset, incomingAsset, incomingHaircut, poolCid, status, createdAt,
        updatedAt).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, SubstitutionRequest> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<SubstitutionRequest> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(13);
    fields.add(new DamlRecord.Field("hedgeFund", new Party(this.hedgeFund)));
    fields.add(new DamlRecord.Field("primeBroker", new Party(this.primeBroker)));
    fields.add(new DamlRecord.Field("custodian", new Party(this.custodian)));
    fields.add(new DamlRecord.Field("riskManager", new Party(this.riskManager)));
    fields.add(new DamlRecord.Field("requestId", new Text(this.requestId)));
    fields.add(new DamlRecord.Field("outgoingPosId", new Text(this.outgoingPosId)));
    fields.add(new DamlRecord.Field("outgoingAsset", this.outgoingAsset.toValue()));
    fields.add(new DamlRecord.Field("incomingAsset", this.incomingAsset.toValue()));
    fields.add(new DamlRecord.Field("incomingHaircut", this.incomingHaircut.toValue()));
    fields.add(new DamlRecord.Field("poolCid", this.poolCid.toValue()));
    fields.add(new DamlRecord.Field("status", this.status.toValue()));
    fields.add(new DamlRecord.Field("createdAt", Timestamp.fromInstant(this.createdAt)));
    fields.add(new DamlRecord.Field("updatedAt", Timestamp.fromInstant(this.updatedAt)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<SubstitutionRequest> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(13,0, recordValue$);
      String hedgeFund = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String primeBroker = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String custodian = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String riskManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(3).getValue());
      String requestId = PrimitiveValueDecoders.fromText.decode(fields$.get(4).getValue());
      String outgoingPosId = PrimitiveValueDecoders.fromText.decode(fields$.get(5).getValue());
      AssetType outgoingAsset = AssetType.valueDecoder().decode(fields$.get(6).getValue());
      AssetType incomingAsset = AssetType.valueDecoder().decode(fields$.get(7).getValue());
      HaircutRate incomingHaircut = HaircutRate.valueDecoder().decode(fields$.get(8).getValue());
      CollateralPool.ContractId poolCid =
          new CollateralPool.ContractId(fields$.get(9).getValue().asContractId().orElseThrow(() -> new IllegalArgumentException("Expected poolCid to be of type com.daml.ledger.javaapi.data.ContractId")).getValue());
      SubstitutionStatus status = SubstitutionStatus.valueDecoder()
          .decode(fields$.get(10).getValue());
      Instant createdAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(11).getValue());
      Instant updatedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(12).getValue());
      return new SubstitutionRequest(hedgeFund, primeBroker, custodian, riskManager, requestId,
          outgoingPosId, outgoingAsset, incomingAsset, incomingHaircut, poolCid, status, createdAt,
          updatedAt);
    } ;
  }

  public static JsonLfDecoder<SubstitutionRequest> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("hedgeFund", "primeBroker", "custodian", "riskManager", "requestId", "outgoingPosId", "outgoingAsset", "incomingAsset", "incomingHaircut", "poolCid", "status", "createdAt", "updatedAt"), name -> {
          switch (name) {
            case "hedgeFund": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "primeBroker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "custodian": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "riskManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "requestId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "outgoingPosId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "outgoingAsset": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, new com.canton.etf.model.canton.etf.primebrokerage.collateralasset.AssetType.JsonDecoder$().get());
            case "incomingAsset": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, new com.canton.etf.model.canton.etf.primebrokerage.collateralasset.AssetType.JsonDecoder$().get());
            case "incomingHaircut": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(8, new com.canton.etf.model.canton.etf.primebrokerage.collateralasset.HaircutRate.JsonDecoder$().get());
            case "poolCid": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(9, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.contractId(com.canton.etf.model.canton.etf.primebrokerage.collateralpool.CollateralPool.ContractId::new));
            case "status": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(10, new com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionStatus.JsonDecoder$().get());
            case "createdAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(11, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            case "updatedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(12, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new SubstitutionRequest(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7]), JsonLfDecoders.cast(args[8]), JsonLfDecoders.cast(args[9]), JsonLfDecoders.cast(args[10]), JsonLfDecoders.cast(args[11]), JsonLfDecoders.cast(args[12])));
  }

  public static SubstitutionRequest fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("hedgeFund", apply(JsonLfEncoders::party, hedgeFund)),
        JsonLfEncoders.Field.of("primeBroker", apply(JsonLfEncoders::party, primeBroker)),
        JsonLfEncoders.Field.of("custodian", apply(JsonLfEncoders::party, custodian)),
        JsonLfEncoders.Field.of("riskManager", apply(JsonLfEncoders::party, riskManager)),
        JsonLfEncoders.Field.of("requestId", apply(JsonLfEncoders::text, requestId)),
        JsonLfEncoders.Field.of("outgoingPosId", apply(JsonLfEncoders::text, outgoingPosId)),
        JsonLfEncoders.Field.of("outgoingAsset", apply(AssetType::jsonEncoder, outgoingAsset)),
        JsonLfEncoders.Field.of("incomingAsset", apply(AssetType::jsonEncoder, incomingAsset)),
        JsonLfEncoders.Field.of("incomingHaircut", apply(HaircutRate::jsonEncoder, incomingHaircut)),
        JsonLfEncoders.Field.of("poolCid", apply(JsonLfEncoders::contractId, poolCid)),
        JsonLfEncoders.Field.of("status", apply(SubstitutionStatus::jsonEncoder, status)),
        JsonLfEncoders.Field.of("createdAt", apply(JsonLfEncoders::timestamp, createdAt)),
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
    if (!(object instanceof SubstitutionRequest)) {
      return false;
    }
    SubstitutionRequest other = (SubstitutionRequest) object;
    return Objects.equals(this.hedgeFund, other.hedgeFund) &&
        Objects.equals(this.primeBroker, other.primeBroker) &&
        Objects.equals(this.custodian, other.custodian) &&
        Objects.equals(this.riskManager, other.riskManager) &&
        Objects.equals(this.requestId, other.requestId) &&
        Objects.equals(this.outgoingPosId, other.outgoingPosId) &&
        Objects.equals(this.outgoingAsset, other.outgoingAsset) &&
        Objects.equals(this.incomingAsset, other.incomingAsset) &&
        Objects.equals(this.incomingHaircut, other.incomingHaircut) &&
        Objects.equals(this.poolCid, other.poolCid) && Objects.equals(this.status, other.status) &&
        Objects.equals(this.createdAt, other.createdAt) &&
        Objects.equals(this.updatedAt, other.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.hedgeFund, this.primeBroker, this.custodian, this.riskManager,
        this.requestId, this.outgoingPosId, this.outgoingAsset, this.incomingAsset,
        this.incomingHaircut, this.poolCid, this.status, this.createdAt, this.updatedAt);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.primebrokerage.substitutionrequest.SubstitutionRequest(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
        this.hedgeFund, this.primeBroker, this.custodian, this.riskManager, this.requestId,
        this.outgoingPosId, this.outgoingAsset, this.incomingAsset, this.incomingHaircut,
        this.poolCid, this.status, this.createdAt, this.updatedAt);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<SubstitutionRequest> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, SubstitutionRequest, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<SubstitutionRequest> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, SubstitutionRequest> {
    public Contract(ContractId id, SubstitutionRequest data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, SubstitutionRequest> getCompanion() {
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
    default Update<Exercised<ContractId>> exerciseApproveByCustodian(ApproveByCustodian arg) {
      return makeExerciseCmd(CHOICE_ApproveByCustodian, arg);
    }

    default Update<Exercised<ContractId>> exerciseApproveByCustodian(String comment) {
      return exerciseApproveByCustodian(new ApproveByCustodian(comment));
    }

    default Update<Exercised<Tuple2<ContractId, CollateralPool.ContractId>>> exerciseConfirmTransfer(
        ConfirmTransfer arg) {
      return makeExerciseCmd(CHOICE_ConfirmTransfer, arg);
    }

    default Update<Exercised<Tuple2<ContractId, CollateralPool.ContractId>>> exerciseConfirmTransfer(
        CollateralPosition newIncomingPosition) {
      return exerciseConfirmTransfer(new ConfirmTransfer(newIncomingPosition));
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<ContractId>> exerciseRejectByBroker(RejectByBroker arg) {
      return makeExerciseCmd(CHOICE_RejectByBroker, arg);
    }

    default Update<Exercised<ContractId>> exerciseRejectByBroker(String reason) {
      return exerciseRejectByBroker(new RejectByBroker(reason));
    }

    default Update<Exercised<ContractId>> exerciseRejectByCustodian(RejectByCustodian arg) {
      return makeExerciseCmd(CHOICE_RejectByCustodian, arg);
    }

    default Update<Exercised<ContractId>> exerciseRejectByCustodian(String reason) {
      return exerciseRejectByCustodian(new RejectByCustodian(reason));
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, SubstitutionRequest, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<SubstitutionRequest> get() {
      return jsonDecoder();
    }
  }
}
