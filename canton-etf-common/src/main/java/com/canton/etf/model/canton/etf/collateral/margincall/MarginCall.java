package com.canton.etf.model.canton.etf.collateral.margincall;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

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

public final class MarginCall extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Collateral.MarginCall", "MarginCall");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18", "Canton.ETF.Collateral.MarginCall", "MarginCall");

  public static final String PACKAGE_ID = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<MarginCall, Meet, ContractId> CHOICE_Meet = 
      Choice.create("Meet", value$ -> value$.toValue(), value$ -> Meet.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new Meet.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        Meet::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<MarginCall, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<MarginCall, Default, ContractId> CHOICE_Default = 
      Choice.create("Default", value$ -> value$.toValue(), value$ -> Default.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new Default.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        Default::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, MarginCall> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(MarginCall.PACKAGE_ID, MarginCall.PACKAGE_NAME, MarginCall.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.collateral.margincall.MarginCall", TEMPLATE_ID,
        ContractId::new, v -> MarginCall.templateValueDecoder().decode(v), MarginCall::fromJson,
        Contract::new, List.of(CHOICE_Meet, CHOICE_Archive, CHOICE_Default));

  public final String custodian;

  public final String fundManager;

  public final String compliance;

  public final String auditor;

  public final String asset;

  public final BigDecimal amountRequired;

  public final Instant issuedAt;

  public final Instant dueBy;

  public final String status;

  public MarginCall(String custodian, String fundManager, String compliance, String auditor,
      String asset, BigDecimal amountRequired, Instant issuedAt, Instant dueBy, String status) {
    this.custodian = custodian;
    this.fundManager = fundManager;
    this.compliance = compliance;
    this.auditor = auditor;
    this.asset = asset;
    this.amountRequired = amountRequired;
    this.issuedAt = issuedAt;
    this.dueBy = dueBy;
    this.status = status;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(MarginCall.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseMeet} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseMeet(Meet arg) {
    return createAnd().exerciseMeet(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseMeet} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseMeet() {
    return createAndExerciseMeet(new Meet());
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseDefault} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseDefault(Default arg) {
    return createAnd().exerciseDefault(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseDefault} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseDefault() {
    return createAndExerciseDefault(new Default());
  }

  public static Update<Created<ContractId>> create(String custodian, String fundManager,
      String compliance, String auditor, String asset, BigDecimal amountRequired, Instant issuedAt,
      Instant dueBy, String status) {
    return new MarginCall(custodian, fundManager, compliance, auditor, asset, amountRequired,
        issuedAt, dueBy, status).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, MarginCall> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<MarginCall> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(9);
    fields.add(new DamlRecord.Field("custodian", new Party(this.custodian)));
    fields.add(new DamlRecord.Field("fundManager", new Party(this.fundManager)));
    fields.add(new DamlRecord.Field("compliance", new Party(this.compliance)));
    fields.add(new DamlRecord.Field("auditor", new Party(this.auditor)));
    fields.add(new DamlRecord.Field("asset", new Text(this.asset)));
    fields.add(new DamlRecord.Field("amountRequired", new Numeric(this.amountRequired)));
    fields.add(new DamlRecord.Field("issuedAt", Timestamp.fromInstant(this.issuedAt)));
    fields.add(new DamlRecord.Field("dueBy", Timestamp.fromInstant(this.dueBy)));
    fields.add(new DamlRecord.Field("status", new Text(this.status)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<MarginCall> templateValueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(9,0, recordValue$);
      String custodian = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String fundManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String compliance = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String auditor = PrimitiveValueDecoders.fromParty.decode(fields$.get(3).getValue());
      String asset = PrimitiveValueDecoders.fromText.decode(fields$.get(4).getValue());
      BigDecimal amountRequired = PrimitiveValueDecoders.fromNumeric
          .decode(fields$.get(5).getValue());
      Instant issuedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(6).getValue());
      Instant dueBy = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(7).getValue());
      String status = PrimitiveValueDecoders.fromText.decode(fields$.get(8).getValue());
      return new MarginCall(custodian, fundManager, compliance, auditor, asset, amountRequired,
          issuedAt, dueBy, status);
    } ;
  }

  public static JsonLfDecoder<MarginCall> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("custodian", "fundManager", "compliance", "auditor", "asset", "amountRequired", "issuedAt", "dueBy", "status"), name -> {
          switch (name) {
            case "custodian": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "fundManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "compliance": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "auditor": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "asset": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "amountRequired": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "issuedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            case "dueBy": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            case "status": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(8, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            default: return null;
          }
        }
        , (Object[] args) -> new MarginCall(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7]), JsonLfDecoders.cast(args[8])));
  }

  public static MarginCall fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("custodian", apply(JsonLfEncoders::party, custodian)),
        JsonLfEncoders.Field.of("fundManager", apply(JsonLfEncoders::party, fundManager)),
        JsonLfEncoders.Field.of("compliance", apply(JsonLfEncoders::party, compliance)),
        JsonLfEncoders.Field.of("auditor", apply(JsonLfEncoders::party, auditor)),
        JsonLfEncoders.Field.of("asset", apply(JsonLfEncoders::text, asset)),
        JsonLfEncoders.Field.of("amountRequired", apply(JsonLfEncoders::numeric, amountRequired)),
        JsonLfEncoders.Field.of("issuedAt", apply(JsonLfEncoders::timestamp, issuedAt)),
        JsonLfEncoders.Field.of("dueBy", apply(JsonLfEncoders::timestamp, dueBy)),
        JsonLfEncoders.Field.of("status", apply(JsonLfEncoders::text, status)));
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
    if (!(object instanceof MarginCall)) {
      return false;
    }
    MarginCall other = (MarginCall) object;
    return Objects.equals(this.custodian, other.custodian) &&
        Objects.equals(this.fundManager, other.fundManager) &&
        Objects.equals(this.compliance, other.compliance) &&
        Objects.equals(this.auditor, other.auditor) && Objects.equals(this.asset, other.asset) &&
        Objects.equals(this.amountRequired, other.amountRequired) &&
        Objects.equals(this.issuedAt, other.issuedAt) && Objects.equals(this.dueBy, other.dueBy) &&
        Objects.equals(this.status, other.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.custodian, this.fundManager, this.compliance, this.auditor, this.asset,
        this.amountRequired, this.issuedAt, this.dueBy, this.status);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.collateral.margincall.MarginCall(%s, %s, %s, %s, %s, %s, %s, %s, %s)",
        this.custodian, this.fundManager, this.compliance, this.auditor, this.asset,
        this.amountRequired, this.issuedAt, this.dueBy, this.status);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<MarginCall> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, MarginCall, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<MarginCall> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, MarginCall> {
    public Contract(ContractId id, MarginCall data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, MarginCall> getCompanion() {
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
    default Update<Exercised<ContractId>> exerciseMeet(Meet arg) {
      return makeExerciseCmd(CHOICE_Meet, arg);
    }

    default Update<Exercised<ContractId>> exerciseMeet() {
      return exerciseMeet(new Meet());
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<ContractId>> exerciseDefault(Default arg) {
      return makeExerciseCmd(CHOICE_Default, arg);
    }

    default Update<Exercised<ContractId>> exerciseDefault() {
      return exerciseDefault(new Default());
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, MarginCall, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<MarginCall> get() {
      return jsonDecoder();
    }
  }
}
