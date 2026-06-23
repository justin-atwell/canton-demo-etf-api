package com.canton.etf.model.canton.etf.fund.etfdefinition;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.fund.constituent.Constituent;
import com.canton.etf.model.da.internal.template.Archive;
import com.daml.ledger.javaapi.data.ContractFilter;
import com.daml.ledger.javaapi.data.CreateAndExerciseCommand;
import com.daml.ledger.javaapi.data.CreateCommand;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.Date;
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
import java.lang.Deprecated;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class ETFDefinition extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Fund.ETFDefinition", "ETFDefinition");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18", "Canton.ETF.Fund.ETFDefinition", "ETFDefinition");

  public static final String PACKAGE_ID = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<ETFDefinition, Suspend, ContractId> CHOICE_Suspend = 
      Choice.create("Suspend", value$ -> value$.toValue(), value$ -> Suspend.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new Suspend.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        Suspend::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<ETFDefinition, Terminate, Unit> CHOICE_Terminate = 
      Choice.create("Terminate", value$ -> value$.toValue(), value$ -> Terminate.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Terminate.JsonDecoder$().get(), JsonLfDecoders.unit, Terminate::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<ETFDefinition, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<ETFDefinition, AddConstituent, Constituent.ContractId> CHOICE_AddConstituent = 
      Choice.create("AddConstituent", value$ -> value$.toValue(), value$ ->
        AddConstituent.valueDecoder().decode(value$), value$ ->
        new Constituent.ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new AddConstituent.JsonDecoder$().get(),
        JsonLfDecoders.contractId(Constituent.ContractId::new), AddConstituent::jsonEncoder,
        JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, ETFDefinition> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(ETFDefinition.PACKAGE_ID, ETFDefinition.PACKAGE_NAME, ETFDefinition.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition", TEMPLATE_ID,
        ContractId::new, v -> ETFDefinition.templateValueDecoder().decode(v),
        ETFDefinition::fromJson, Contract::new, List.of(CHOICE_Suspend, CHOICE_Terminate,
        CHOICE_Archive, CHOICE_AddConstituent));

  public final String fundManager;

  public final String custodian;

  public final String compliance;

  public final String auditor;

  public final String ticker;

  public final String name;

  public final String cusip;

  public final String status;

  public final LocalDate inceptionDate;

  public ETFDefinition(String fundManager, String custodian, String compliance, String auditor,
      String ticker, String name, String cusip, String status, LocalDate inceptionDate) {
    this.fundManager = fundManager;
    this.custodian = custodian;
    this.compliance = compliance;
    this.auditor = auditor;
    this.ticker = ticker;
    this.name = name;
    this.cusip = cusip;
    this.status = status;
    this.inceptionDate = inceptionDate;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(ETFDefinition.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseSuspend} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseSuspend(Suspend arg) {
    return createAnd().exerciseSuspend(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseSuspend} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseSuspend() {
    return createAndExerciseSuspend(new Suspend());
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseTerminate} instead
   */
  @Deprecated
  public Update<Exercised<Unit>> createAndExerciseTerminate(Terminate arg) {
    return createAnd().exerciseTerminate(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseTerminate} instead
   */
  @Deprecated
  public Update<Exercised<Unit>> createAndExerciseTerminate() {
    return createAndExerciseTerminate(new Terminate());
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseAddConstituent} instead
   */
  @Deprecated
  public Update<Exercised<Constituent.ContractId>> createAndExerciseAddConstituent(
      AddConstituent arg) {
    return createAnd().exerciseAddConstituent(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseAddConstituent} instead
   */
  @Deprecated
  public Update<Exercised<Constituent.ContractId>> createAndExerciseAddConstituent(String symbol,
      String name, String cusip, BigDecimal weight) {
    return createAndExerciseAddConstituent(new AddConstituent(symbol, name, cusip, weight));
  }

  public static Update<Created<ContractId>> create(String fundManager, String custodian,
      String compliance, String auditor, String ticker, String name, String cusip, String status,
      LocalDate inceptionDate) {
    return new ETFDefinition(fundManager, custodian, compliance, auditor, ticker, name, cusip,
        status, inceptionDate).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, ETFDefinition> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<ETFDefinition> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(9);
    fields.add(new DamlRecord.Field("fundManager", new Party(this.fundManager)));
    fields.add(new DamlRecord.Field("custodian", new Party(this.custodian)));
    fields.add(new DamlRecord.Field("compliance", new Party(this.compliance)));
    fields.add(new DamlRecord.Field("auditor", new Party(this.auditor)));
    fields.add(new DamlRecord.Field("ticker", new Text(this.ticker)));
    fields.add(new DamlRecord.Field("name", new Text(this.name)));
    fields.add(new DamlRecord.Field("cusip", new Text(this.cusip)));
    fields.add(new DamlRecord.Field("status", new Text(this.status)));
    fields.add(new DamlRecord.Field("inceptionDate", new Date((int) this.inceptionDate.toEpochDay())));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<ETFDefinition> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(9,0, recordValue$);
      String fundManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String custodian = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String compliance = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String auditor = PrimitiveValueDecoders.fromParty.decode(fields$.get(3).getValue());
      String ticker = PrimitiveValueDecoders.fromText.decode(fields$.get(4).getValue());
      String name = PrimitiveValueDecoders.fromText.decode(fields$.get(5).getValue());
      String cusip = PrimitiveValueDecoders.fromText.decode(fields$.get(6).getValue());
      String status = PrimitiveValueDecoders.fromText.decode(fields$.get(7).getValue());
      LocalDate inceptionDate = PrimitiveValueDecoders.fromDate.decode(fields$.get(8).getValue());
      return new ETFDefinition(fundManager, custodian, compliance, auditor, ticker, name, cusip,
          status, inceptionDate);
    } ;
  }

  public static JsonLfDecoder<ETFDefinition> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("fundManager", "custodian", "compliance", "auditor", "ticker", "name", "cusip", "status", "inceptionDate"), name -> {
          switch (name) {
            case "fundManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "custodian": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "compliance": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "auditor": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "ticker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "name": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "cusip": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "status": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "inceptionDate": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(8, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.date);
            default: return null;
          }
        }
        , (Object[] args) -> new ETFDefinition(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7]), JsonLfDecoders.cast(args[8])));
  }

  public static ETFDefinition fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("fundManager", apply(JsonLfEncoders::party, fundManager)),
        JsonLfEncoders.Field.of("custodian", apply(JsonLfEncoders::party, custodian)),
        JsonLfEncoders.Field.of("compliance", apply(JsonLfEncoders::party, compliance)),
        JsonLfEncoders.Field.of("auditor", apply(JsonLfEncoders::party, auditor)),
        JsonLfEncoders.Field.of("ticker", apply(JsonLfEncoders::text, ticker)),
        JsonLfEncoders.Field.of("name", apply(JsonLfEncoders::text, name)),
        JsonLfEncoders.Field.of("cusip", apply(JsonLfEncoders::text, cusip)),
        JsonLfEncoders.Field.of("status", apply(JsonLfEncoders::text, status)),
        JsonLfEncoders.Field.of("inceptionDate", apply(JsonLfEncoders::date, inceptionDate)));
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
    if (!(object instanceof ETFDefinition)) {
      return false;
    }
    ETFDefinition other = (ETFDefinition) object;
    return Objects.equals(this.fundManager, other.fundManager) &&
        Objects.equals(this.custodian, other.custodian) &&
        Objects.equals(this.compliance, other.compliance) &&
        Objects.equals(this.auditor, other.auditor) && Objects.equals(this.ticker, other.ticker) &&
        Objects.equals(this.name, other.name) && Objects.equals(this.cusip, other.cusip) &&
        Objects.equals(this.status, other.status) &&
        Objects.equals(this.inceptionDate, other.inceptionDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fundManager, this.custodian, this.compliance, this.auditor,
        this.ticker, this.name, this.cusip, this.status, this.inceptionDate);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.fund.etfdefinition.ETFDefinition(%s, %s, %s, %s, %s, %s, %s, %s, %s)",
        this.fundManager, this.custodian, this.compliance, this.auditor, this.ticker, this.name,
        this.cusip, this.status, this.inceptionDate);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<ETFDefinition> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, ETFDefinition, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<ETFDefinition> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ETFDefinition> {
    public Contract(ContractId id, ETFDefinition data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, ETFDefinition> getCompanion() {
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
    default Update<Exercised<ContractId>> exerciseSuspend(Suspend arg) {
      return makeExerciseCmd(CHOICE_Suspend, arg);
    }

    default Update<Exercised<ContractId>> exerciseSuspend() {
      return exerciseSuspend(new Suspend());
    }

    default Update<Exercised<Unit>> exerciseTerminate(Terminate arg) {
      return makeExerciseCmd(CHOICE_Terminate, arg);
    }

    default Update<Exercised<Unit>> exerciseTerminate() {
      return exerciseTerminate(new Terminate());
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<Constituent.ContractId>> exerciseAddConstituent(AddConstituent arg) {
      return makeExerciseCmd(CHOICE_AddConstituent, arg);
    }

    default Update<Exercised<Constituent.ContractId>> exerciseAddConstituent(String symbol,
        String name, String cusip, BigDecimal weight) {
      return exerciseAddConstituent(new AddConstituent(symbol, name, cusip, weight));
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, ETFDefinition, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<ETFDefinition> get() {
      return jsonDecoder();
    }
  }
}
