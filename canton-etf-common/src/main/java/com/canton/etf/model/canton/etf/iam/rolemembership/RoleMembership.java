package com.canton.etf.model.canton.etf.iam.rolemembership;

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

public final class RoleMembership extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.IAM.RoleMembership", "RoleMembership");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("1f28d7b7af3161c75a78b37a77e89a8c08d9579c841613879009c4365fd6566c", "Canton.ETF.IAM.RoleMembership", "RoleMembership");

  public static final String PACKAGE_ID = "1f28d7b7af3161c75a78b37a77e89a8c08d9579c841613879009c4365fd6566c";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<RoleMembership, Revoke, Unit> CHOICE_Revoke = 
      Choice.create("Revoke", value$ -> value$.toValue(), value$ -> Revoke.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Revoke.JsonDecoder$().get(), JsonLfDecoders.unit, Revoke::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<RoleMembership, TransferOperator, ContractId> CHOICE_TransferOperator = 
      Choice.create("TransferOperator", value$ -> value$.toValue(), value$ ->
        TransferOperator.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new TransferOperator.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        TransferOperator::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<RoleMembership, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, RoleMembership> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(RoleMembership.PACKAGE_ID, RoleMembership.PACKAGE_NAME, RoleMembership.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.iam.rolemembership.RoleMembership", TEMPLATE_ID,
        ContractId::new, v -> RoleMembership.templateValueDecoder().decode(v),
        RoleMembership::fromJson, Contract::new, List.of(CHOICE_Revoke, CHOICE_TransferOperator,
        CHOICE_Archive));

  public final String operator;

  public final String grantee;

  public final String role;

  public final Instant grantedAt;

  public RoleMembership(String operator, String grantee, String role, Instant grantedAt) {
    this.operator = operator;
    this.grantee = grantee;
    this.role = role;
    this.grantedAt = grantedAt;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(RoleMembership.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRevoke} instead
   */
  @Deprecated
  public Update<Exercised<Unit>> createAndExerciseRevoke(Revoke arg) {
    return createAnd().exerciseRevoke(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseRevoke} instead
   */
  @Deprecated
  public Update<Exercised<Unit>> createAndExerciseRevoke() {
    return createAndExerciseRevoke(new Revoke());
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseTransferOperator} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseTransferOperator(TransferOperator arg) {
    return createAnd().exerciseTransferOperator(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseTransferOperator} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseTransferOperator(String newOperator) {
    return createAndExerciseTransferOperator(new TransferOperator(newOperator));
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

  public static Update<Created<ContractId>> create(String operator, String grantee, String role,
      Instant grantedAt) {
    return new RoleMembership(operator, grantee, role, grantedAt).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, RoleMembership> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<RoleMembership> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(4);
    fields.add(new DamlRecord.Field("operator", new Party(this.operator)));
    fields.add(new DamlRecord.Field("grantee", new Party(this.grantee)));
    fields.add(new DamlRecord.Field("role", new Text(this.role)));
    fields.add(new DamlRecord.Field("grantedAt", Timestamp.fromInstant(this.grantedAt)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<RoleMembership> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(4,0, recordValue$);
      String operator = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String grantee = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String role = PrimitiveValueDecoders.fromText.decode(fields$.get(2).getValue());
      Instant grantedAt = PrimitiveValueDecoders.fromTimestamp.decode(fields$.get(3).getValue());
      return new RoleMembership(operator, grantee, role, grantedAt);
    } ;
  }

  public static JsonLfDecoder<RoleMembership> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("operator", "grantee", "role", "grantedAt"), name -> {
          switch (name) {
            case "operator": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "grantee": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "role": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "grantedAt": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.timestamp);
            default: return null;
          }
        }
        , (Object[] args) -> new RoleMembership(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3])));
  }

  public static RoleMembership fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("operator", apply(JsonLfEncoders::party, operator)),
        JsonLfEncoders.Field.of("grantee", apply(JsonLfEncoders::party, grantee)),
        JsonLfEncoders.Field.of("role", apply(JsonLfEncoders::text, role)),
        JsonLfEncoders.Field.of("grantedAt", apply(JsonLfEncoders::timestamp, grantedAt)));
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
    if (!(object instanceof RoleMembership)) {
      return false;
    }
    RoleMembership other = (RoleMembership) object;
    return Objects.equals(this.operator, other.operator) &&
        Objects.equals(this.grantee, other.grantee) && Objects.equals(this.role, other.role) &&
        Objects.equals(this.grantedAt, other.grantedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.operator, this.grantee, this.role, this.grantedAt);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.iam.rolemembership.RoleMembership(%s, %s, %s, %s)",
        this.operator, this.grantee, this.role, this.grantedAt);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<RoleMembership> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, RoleMembership, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<RoleMembership> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, RoleMembership> {
    public Contract(ContractId id, RoleMembership data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, RoleMembership> getCompanion() {
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
    default Update<Exercised<Unit>> exerciseRevoke(Revoke arg) {
      return makeExerciseCmd(CHOICE_Revoke, arg);
    }

    default Update<Exercised<Unit>> exerciseRevoke() {
      return exerciseRevoke(new Revoke());
    }

    default Update<Exercised<ContractId>> exerciseTransferOperator(TransferOperator arg) {
      return makeExerciseCmd(CHOICE_TransferOperator, arg);
    }

    default Update<Exercised<ContractId>> exerciseTransferOperator(String newOperator) {
      return exerciseTransferOperator(new TransferOperator(newOperator));
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
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, RoleMembership, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<RoleMembership> get() {
      return jsonDecoder();
    }
  }
}
