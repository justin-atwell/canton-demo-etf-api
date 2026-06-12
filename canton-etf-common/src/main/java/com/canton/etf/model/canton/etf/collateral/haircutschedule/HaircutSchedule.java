package com.canton.etf.model.canton.etf.collateral.haircutschedule;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class HaircutSchedule extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Collateral.HaircutSchedule", "HaircutSchedule");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("c2e41b9e13dba8a6699eb8b7cde2c5e5c96ac00b38b18aa67373f159da67c93d", "Canton.ETF.Collateral.HaircutSchedule", "HaircutSchedule");

  public static final String PACKAGE_ID = "c2e41b9e13dba8a6699eb8b7cde2c5e5c96ac00b38b18aa67373f159da67c93d";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<HaircutSchedule, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<HaircutSchedule, UpdateHaircut, ContractId> CHOICE_UpdateHaircut = 
      Choice.create("UpdateHaircut", value$ -> value$.toValue(), value$ ->
        UpdateHaircut.valueDecoder().decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new UpdateHaircut.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        UpdateHaircut::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, HaircutSchedule> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(HaircutSchedule.PACKAGE_ID, HaircutSchedule.PACKAGE_NAME, HaircutSchedule.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.collateral.haircutschedule.HaircutSchedule", TEMPLATE_ID,
        ContractId::new, v -> HaircutSchedule.templateValueDecoder().decode(v),
        HaircutSchedule::fromJson, Contract::new, List.of(CHOICE_Archive, CHOICE_UpdateHaircut));

  public final String compliance;

  public final String auditor;

  public final String scheduleId;

  public final List<Tuple2<String, BigDecimal>> entries;

  public HaircutSchedule(String compliance, String auditor, String scheduleId,
      List<Tuple2<String, BigDecimal>> entries) {
    this.compliance = compliance;
    this.auditor = auditor;
    this.scheduleId = scheduleId;
    this.entries = entries;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(HaircutSchedule.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateHaircut} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateHaircut(UpdateHaircut arg) {
    return createAnd().exerciseUpdateHaircut(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseUpdateHaircut} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseUpdateHaircut(String asset,
      BigDecimal rate) {
    return createAndExerciseUpdateHaircut(new UpdateHaircut(asset, rate));
  }

  public static Update<Created<ContractId>> create(String compliance, String auditor,
      String scheduleId, List<Tuple2<String, BigDecimal>> entries) {
    return new HaircutSchedule(compliance, auditor, scheduleId, entries).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, HaircutSchedule> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<HaircutSchedule> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(4);
    fields.add(new DamlRecord.Field("compliance", new Party(this.compliance)));
    fields.add(new DamlRecord.Field("auditor", new Party(this.auditor)));
    fields.add(new DamlRecord.Field("scheduleId", new Text(this.scheduleId)));
    fields.add(new DamlRecord.Field("entries", this.entries.stream().collect(DamlCollectors.toDamlList(v$0 -> v$0.toValue(v$1 -> new Text(v$1),
        v$2 -> new Numeric(v$2))))));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<HaircutSchedule> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(4,0, recordValue$);
      String compliance = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String auditor = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String scheduleId = PrimitiveValueDecoders.fromText.decode(fields$.get(2).getValue());
      List<Tuple2<String, BigDecimal>> entries = PrimitiveValueDecoders.fromList(
            Tuple2.<java.lang.String,
            java.math.BigDecimal>valueDecoder(PrimitiveValueDecoders.fromText,
            PrimitiveValueDecoders.fromNumeric)).decode(fields$.get(3).getValue());
      return new HaircutSchedule(compliance, auditor, scheduleId, entries);
    } ;
  }

  public static JsonLfDecoder<HaircutSchedule> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("compliance", "auditor", "scheduleId", "entries"), name -> {
          switch (name) {
            case "compliance": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "auditor": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "scheduleId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "entries": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.list(new com.canton.etf.model.da.types.Tuple2.JsonDecoder$().get(com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10))));
            default: return null;
          }
        }
        , (Object[] args) -> new HaircutSchedule(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3])));
  }

  public static HaircutSchedule fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("compliance", apply(JsonLfEncoders::party, compliance)),
        JsonLfEncoders.Field.of("auditor", apply(JsonLfEncoders::party, auditor)),
        JsonLfEncoders.Field.of("scheduleId", apply(JsonLfEncoders::text, scheduleId)),
        JsonLfEncoders.Field.of("entries", apply(JsonLfEncoders.list(_x1 -> _x1.jsonEncoder(JsonLfEncoders::text, JsonLfEncoders::numeric)), entries)));
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
    if (!(object instanceof HaircutSchedule)) {
      return false;
    }
    HaircutSchedule other = (HaircutSchedule) object;
    return Objects.equals(this.compliance, other.compliance) &&
        Objects.equals(this.auditor, other.auditor) &&
        Objects.equals(this.scheduleId, other.scheduleId) &&
        Objects.equals(this.entries, other.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.compliance, this.auditor, this.scheduleId, this.entries);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.collateral.haircutschedule.HaircutSchedule(%s, %s, %s, %s)",
        this.compliance, this.auditor, this.scheduleId, this.entries);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<HaircutSchedule> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, HaircutSchedule, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<HaircutSchedule> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, HaircutSchedule> {
    public Contract(ContractId id, HaircutSchedule data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, HaircutSchedule> getCompanion() {
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

    default Update<Exercised<ContractId>> exerciseUpdateHaircut(UpdateHaircut arg) {
      return makeExerciseCmd(CHOICE_UpdateHaircut, arg);
    }

    default Update<Exercised<ContractId>> exerciseUpdateHaircut(String asset, BigDecimal rate) {
      return exerciseUpdateHaircut(new UpdateHaircut(asset, rate));
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, HaircutSchedule, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<HaircutSchedule> get() {
      return jsonDecoder();
    }
  }
}
