package com.canton.etf.model.canton.etf.iam.directoryentry;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.da.internal.template.Archive;
import com.daml.ledger.javaapi.data.Bool;
import com.daml.ledger.javaapi.data.ContractFilter;
import com.daml.ledger.javaapi.data.CreateAndExerciseCommand;
import com.daml.ledger.javaapi.data.CreateCommand;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.DamlCollectors;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.ExerciseCommand;
import com.daml.ledger.javaapi.data.Identifier;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class DirectoryEntry extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.IAM.DirectoryEntry", "DirectoryEntry");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2", "Canton.ETF.IAM.DirectoryEntry", "DirectoryEntry");

  public static final String PACKAGE_ID = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<DirectoryEntry, Deactivate, ContractId> CHOICE_Deactivate = 
      Choice.create("Deactivate", value$ -> value$.toValue(), value$ -> Deactivate.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new Deactivate.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        Deactivate::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<DirectoryEntry, UpdateRoles, ContractId> CHOICE_UpdateRoles = 
      Choice.create("UpdateRoles", value$ -> value$.toValue(), value$ -> UpdateRoles.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new UpdateRoles.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        UpdateRoles::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<DirectoryEntry, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<DirectoryEntry, Reactivate, ContractId> CHOICE_Reactivate = 
      Choice.create("Reactivate", value$ -> value$.toValue(), value$ -> Reactivate.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new Reactivate.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        Reactivate::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, DirectoryEntry> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(DirectoryEntry.PACKAGE_ID, DirectoryEntry.PACKAGE_NAME, DirectoryEntry.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.iam.directoryentry.DirectoryEntry", TEMPLATE_ID,
        ContractId::new, v -> DirectoryEntry.templateValueDecoder().decode(v),
        DirectoryEntry::fromJson, Contract::new, List.of(CHOICE_Deactivate, CHOICE_UpdateRoles,
        CHOICE_Archive, CHOICE_Reactivate));

  public final String operator;

  public final String subject;

  public final String ldapDn;

  public final List<String> roles;

  public final Boolean active;

  public DirectoryEntry(String operator, String subject, String ldapDn, List<String> roles,
      Boolean active) {
    this.operator = operator;
    this.subject = subject;
    this.ldapDn = ldapDn;
    this.roles = roles;
    this.active = active;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(DirectoryEntry.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseDeactivate} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseDeactivate(Deactivate arg) {
    return createAnd().exerciseDeactivate(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseDeactivate} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseDeactivate() {
    return createAndExerciseDeactivate(new Deactivate());
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateRoles} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateRoles(UpdateRoles arg) {
    return createAnd().exerciseUpdateRoles(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateRoles} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateRoles(List<String> newRoles) {
    return createAndExerciseUpdateRoles(new UpdateRoles(newRoles));
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseReactivate} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseReactivate(Reactivate arg) {
    return createAnd().exerciseReactivate(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseReactivate} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseReactivate() {
    return createAndExerciseReactivate(new Reactivate());
  }

  public static Update<Created<ContractId>> create(String operator, String subject, String ldapDn,
      List<String> roles, Boolean active) {
    return new DirectoryEntry(operator, subject, ldapDn, roles, active).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, DirectoryEntry> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<DirectoryEntry> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(5);
    fields.add(new DamlRecord.Field("operator", new Party(this.operator)));
    fields.add(new DamlRecord.Field("subject", new Party(this.subject)));
    fields.add(new DamlRecord.Field("ldapDn", new Text(this.ldapDn)));
    fields.add(new DamlRecord.Field("roles", this.roles.stream().collect(DamlCollectors.toDamlList(v$0 -> new Text(v$0)))));
    fields.add(new DamlRecord.Field("active", Bool.of(this.active)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<DirectoryEntry> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(5,0, recordValue$);
      String operator = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String subject = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String ldapDn = PrimitiveValueDecoders.fromText.decode(fields$.get(2).getValue());
      List<String> roles = PrimitiveValueDecoders.fromList(PrimitiveValueDecoders.fromText)
          .decode(fields$.get(3).getValue());
      Boolean active = PrimitiveValueDecoders.fromBool.decode(fields$.get(4).getValue());
      return new DirectoryEntry(operator, subject, ldapDn, roles, active);
    } ;
  }

  public static JsonLfDecoder<DirectoryEntry> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("operator", "subject", "ldapDn", "roles", "active"), name -> {
          switch (name) {
            case "operator": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "subject": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "ldapDn": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "roles": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.list(com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text));
            case "active": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.bool);
            default: return null;
          }
        }
        , (Object[] args) -> new DirectoryEntry(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4])));
  }

  public static DirectoryEntry fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("operator", apply(JsonLfEncoders::party, operator)),
        JsonLfEncoders.Field.of("subject", apply(JsonLfEncoders::party, subject)),
        JsonLfEncoders.Field.of("ldapDn", apply(JsonLfEncoders::text, ldapDn)),
        JsonLfEncoders.Field.of("roles", apply(JsonLfEncoders.list(JsonLfEncoders::text), roles)),
        JsonLfEncoders.Field.of("active", apply(JsonLfEncoders::bool, active)));
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
    if (!(object instanceof DirectoryEntry)) {
      return false;
    }
    DirectoryEntry other = (DirectoryEntry) object;
    return Objects.equals(this.operator, other.operator) &&
        Objects.equals(this.subject, other.subject) && Objects.equals(this.ldapDn, other.ldapDn) &&
        Objects.equals(this.roles, other.roles) && Objects.equals(this.active, other.active);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.operator, this.subject, this.ldapDn, this.roles, this.active);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.iam.directoryentry.DirectoryEntry(%s, %s, %s, %s, %s)",
        this.operator, this.subject, this.ldapDn, this.roles, this.active);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<DirectoryEntry> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, DirectoryEntry, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<DirectoryEntry> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, DirectoryEntry> {
    public Contract(ContractId id, DirectoryEntry data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, DirectoryEntry> getCompanion() {
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
    default Update<Exercised<ContractId>> exerciseDeactivate(Deactivate arg) {
      return makeExerciseCmd(CHOICE_Deactivate, arg);
    }

    default Update<Exercised<ContractId>> exerciseDeactivate() {
      return exerciseDeactivate(new Deactivate());
    }

    default Update<Exercised<ContractId>> exerciseUpdateRoles(UpdateRoles arg) {
      return makeExerciseCmd(CHOICE_UpdateRoles, arg);
    }

    default Update<Exercised<ContractId>> exerciseUpdateRoles(List<String> newRoles) {
      return exerciseUpdateRoles(new UpdateRoles(newRoles));
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<ContractId>> exerciseReactivate(Reactivate arg) {
      return makeExerciseCmd(CHOICE_Reactivate, arg);
    }

    default Update<Exercised<ContractId>> exerciseReactivate() {
      return exerciseReactivate(new Reactivate());
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, DirectoryEntry, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<DirectoryEntry> get() {
      return jsonDecoder();
    }
  }
}
