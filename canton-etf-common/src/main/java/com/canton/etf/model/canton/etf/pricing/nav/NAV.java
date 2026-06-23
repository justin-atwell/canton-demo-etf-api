package com.canton.etf.model.canton.etf.pricing.nav;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.da.internal.template.Archive;
import com.daml.ledger.javaapi.data.ContractFilter;
import com.daml.ledger.javaapi.data.CreateAndExerciseCommand;
import com.daml.ledger.javaapi.data.CreateCommand;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.DamlRecord;
import com.daml.ledger.javaapi.data.Date;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class NAV extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Pricing.NAV", "NAV");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18", "Canton.ETF.Pricing.NAV", "NAV");

  public static final String PACKAGE_ID = "d152e05f0163f6f98cd1bfa4dea83fe4efd885a5d3898947b1f6cd243a818b18";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<NAV, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, NAV> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(NAV.PACKAGE_ID, NAV.PACKAGE_NAME, NAV.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.pricing.nav.NAV", TEMPLATE_ID, ContractId::new,
        v -> NAV.templateValueDecoder().decode(v), NAV::fromJson, Contract::new,
        List.of(CHOICE_Archive));

  public final String fundManager;

  public final String custodian;

  public final String compliance;

  public final String auditor;

  public final String ticker;

  public final LocalDate navDate;

  public final BigDecimal navPerShare;

  public final BigDecimal totalAUM;

  public final String source;

  public NAV(String fundManager, String custodian, String compliance, String auditor, String ticker,
      LocalDate navDate, BigDecimal navPerShare, BigDecimal totalAUM, String source) {
    this.fundManager = fundManager;
    this.custodian = custodian;
    this.compliance = compliance;
    this.auditor = auditor;
    this.ticker = ticker;
    this.navDate = navDate;
    this.navPerShare = navPerShare;
    this.totalAUM = totalAUM;
    this.source = source;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(NAV.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
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

  public static Update<Created<ContractId>> create(String fundManager, String custodian,
      String compliance, String auditor, String ticker, LocalDate navDate, BigDecimal navPerShare,
      BigDecimal totalAUM, String source) {
    return new NAV(fundManager, custodian, compliance, auditor, ticker, navDate, navPerShare,
        totalAUM, source).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, NAV> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<NAV> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(9);
    fields.add(new DamlRecord.Field("fundManager", new Party(this.fundManager)));
    fields.add(new DamlRecord.Field("custodian", new Party(this.custodian)));
    fields.add(new DamlRecord.Field("compliance", new Party(this.compliance)));
    fields.add(new DamlRecord.Field("auditor", new Party(this.auditor)));
    fields.add(new DamlRecord.Field("ticker", new Text(this.ticker)));
    fields.add(new DamlRecord.Field("navDate", new Date((int) this.navDate.toEpochDay())));
    fields.add(new DamlRecord.Field("navPerShare", new Numeric(this.navPerShare)));
    fields.add(new DamlRecord.Field("totalAUM", new Numeric(this.totalAUM)));
    fields.add(new DamlRecord.Field("source", new Text(this.source)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<NAV> templateValueDecoder() throws IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(9,0, recordValue$);
      String fundManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String custodian = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String compliance = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String auditor = PrimitiveValueDecoders.fromParty.decode(fields$.get(3).getValue());
      String ticker = PrimitiveValueDecoders.fromText.decode(fields$.get(4).getValue());
      LocalDate navDate = PrimitiveValueDecoders.fromDate.decode(fields$.get(5).getValue());
      BigDecimal navPerShare = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(6).getValue());
      BigDecimal totalAUM = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(7).getValue());
      String source = PrimitiveValueDecoders.fromText.decode(fields$.get(8).getValue());
      return new NAV(fundManager, custodian, compliance, auditor, ticker, navDate, navPerShare,
          totalAUM, source);
    } ;
  }

  public static JsonLfDecoder<NAV> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("fundManager", "custodian", "compliance", "auditor", "ticker", "navDate", "navPerShare", "totalAUM", "source"), name -> {
          switch (name) {
            case "fundManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "custodian": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "compliance": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "auditor": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "ticker": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "navDate": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.date);
            case "navPerShare": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "totalAUM": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(7, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "source": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(8, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            default: return null;
          }
        }
        , (Object[] args) -> new NAV(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6]), JsonLfDecoders.cast(args[7]), JsonLfDecoders.cast(args[8])));
  }

  public static NAV fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("fundManager", apply(JsonLfEncoders::party, fundManager)),
        JsonLfEncoders.Field.of("custodian", apply(JsonLfEncoders::party, custodian)),
        JsonLfEncoders.Field.of("compliance", apply(JsonLfEncoders::party, compliance)),
        JsonLfEncoders.Field.of("auditor", apply(JsonLfEncoders::party, auditor)),
        JsonLfEncoders.Field.of("ticker", apply(JsonLfEncoders::text, ticker)),
        JsonLfEncoders.Field.of("navDate", apply(JsonLfEncoders::date, navDate)),
        JsonLfEncoders.Field.of("navPerShare", apply(JsonLfEncoders::numeric, navPerShare)),
        JsonLfEncoders.Field.of("totalAUM", apply(JsonLfEncoders::numeric, totalAUM)),
        JsonLfEncoders.Field.of("source", apply(JsonLfEncoders::text, source)));
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
    if (!(object instanceof NAV)) {
      return false;
    }
    NAV other = (NAV) object;
    return Objects.equals(this.fundManager, other.fundManager) &&
        Objects.equals(this.custodian, other.custodian) &&
        Objects.equals(this.compliance, other.compliance) &&
        Objects.equals(this.auditor, other.auditor) && Objects.equals(this.ticker, other.ticker) &&
        Objects.equals(this.navDate, other.navDate) &&
        Objects.equals(this.navPerShare, other.navPerShare) &&
        Objects.equals(this.totalAUM, other.totalAUM) && Objects.equals(this.source, other.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fundManager, this.custodian, this.compliance, this.auditor,
        this.ticker, this.navDate, this.navPerShare, this.totalAUM, this.source);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.pricing.nav.NAV(%s, %s, %s, %s, %s, %s, %s, %s, %s)",
        this.fundManager, this.custodian, this.compliance, this.auditor, this.ticker, this.navDate,
        this.navPerShare, this.totalAUM, this.source);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<NAV> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, NAV, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<NAV> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, NAV> {
    public Contract(ContractId id, NAV data, Set<String> signatories, Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, NAV> getCompanion() {
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
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, NAV, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<NAV> get() {
      return jsonDecoder();
    }
  }
}
