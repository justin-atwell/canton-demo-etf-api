package com.canton.etf.model.canton.etf.collateral.collateralaccount;

import static com.daml.ledger.javaapi.data.codegen.json.JsonLfEncoders.apply;

import com.canton.etf.model.canton.etf.collateral.collaterallock.CollateralLock;
import com.canton.etf.model.da.internal.template.Archive;
import com.canton.etf.model.da.types.Tuple2;
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

public final class CollateralAccount extends Template {
  public static final Identifier TEMPLATE_ID = new Identifier("#canton-demo-etf", "Canton.ETF.Collateral.CollateralAccount", "CollateralAccount");

  public static final Identifier TEMPLATE_ID_WITH_PACKAGE_ID = new Identifier("a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2", "Canton.ETF.Collateral.CollateralAccount", "CollateralAccount");

  public static final String PACKAGE_ID = "a2a966e2266e50a56a1ccc2c3f61201f19edb87e6611e193c1e4f4018a9c6ca2";

  public static final String PACKAGE_NAME = "canton-demo-etf";

  public static final PackageVersion PACKAGE_VERSION = new PackageVersion(new int[] {0, 1, 0});

  public static final Choice<CollateralAccount, Deposit, ContractId> CHOICE_Deposit = 
      Choice.create("Deposit", value$ -> value$.toValue(), value$ -> Deposit.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new Deposit.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        Deposit::jsonEncoder, JsonLfEncoders::contractId);

  public static final Choice<CollateralAccount, Lock, Tuple2<ContractId, CollateralLock.ContractId>> CHOICE_Lock = 
      Choice.create("Lock", value$ -> value$.toValue(), value$ -> Lock.valueDecoder()
        .decode(value$), value$ ->
        Tuple2.<com.canton.etf.model.canton.etf.collateral.collateralaccount.CollateralAccount.ContractId,
        com.canton.etf.model.canton.etf.collateral.collaterallock.CollateralLock.ContractId>valueDecoder(v$0 ->
          new ContractId(v$0.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        v$1 ->
          new CollateralLock.ContractId(v$1.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()))
        .decode(value$), new Lock.JsonDecoder$().get(),
        new Tuple2.JsonDecoder$().get(JsonLfDecoders.contractId(ContractId::new), JsonLfDecoders.contractId(CollateralLock.ContractId::new)),
        Lock::jsonEncoder,
        _x0 -> _x0.jsonEncoder(JsonLfEncoders::contractId, JsonLfEncoders::contractId));

  public static final Choice<CollateralAccount, Archive, Unit> CHOICE_Archive = 
      Choice.create("Archive", value$ -> value$.toValue(), value$ -> Archive.valueDecoder()
        .decode(value$), value$ -> PrimitiveValueDecoders.fromUnit.decode(value$),
        new Archive.JsonDecoder$().get(), JsonLfDecoders.unit, Archive::jsonEncoder,
        JsonLfEncoders::unit);

  public static final Choice<CollateralAccount, Withdraw, ContractId> CHOICE_Withdraw = 
      Choice.create("Withdraw", value$ -> value$.toValue(), value$ -> Withdraw.valueDecoder()
        .decode(value$), value$ ->
        new ContractId(value$.asContractId().orElseThrow(() -> new IllegalArgumentException("Expected value$ to be of type com.daml.ledger.javaapi.data.ContractId")).getValue()),
        new Withdraw.JsonDecoder$().get(), JsonLfDecoders.contractId(ContractId::new),
        Withdraw::jsonEncoder, JsonLfEncoders::contractId);

  public static final ContractCompanion.WithoutKey<Contract, ContractId, CollateralAccount> COMPANION = 
      new ContractCompanion.WithoutKey<>(new ContractTypeCompanion.Package(CollateralAccount.PACKAGE_ID, CollateralAccount.PACKAGE_NAME, CollateralAccount.PACKAGE_VERSION),
        "com.canton.etf.model.canton.etf.collateral.collateralaccount.CollateralAccount",
        TEMPLATE_ID, ContractId::new, v -> CollateralAccount.templateValueDecoder().decode(v),
        CollateralAccount::fromJson, Contract::new, List.of(CHOICE_Deposit, CHOICE_Lock,
        CHOICE_Archive, CHOICE_Withdraw));

  public final String custodian;

  public final String fundManager;

  public final String compliance;

  public final String auditor;

  public final String asset;

  public final BigDecimal balance;

  public final String accountId;

  public CollateralAccount(String custodian, String fundManager, String compliance, String auditor,
      String asset, BigDecimal balance, String accountId) {
    this.custodian = custodian;
    this.fundManager = fundManager;
    this.compliance = compliance;
    this.auditor = auditor;
    this.asset = asset;
    this.balance = balance;
    this.accountId = accountId;
  }

  @Override
  public Update<Created<ContractId>> create() {
    return new Update.CreateUpdate<ContractId, Created<ContractId>>(new CreateCommand(CollateralAccount.TEMPLATE_ID, this.toValue()), x -> x, ContractId::new);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseDeposit} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseDeposit(Deposit arg) {
    return createAnd().exerciseDeposit(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseDeposit} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseDeposit(BigDecimal amount) {
    return createAndExerciseDeposit(new Deposit(amount));
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseLock} instead
   */
  @Deprecated
  public Update<Exercised<Tuple2<ContractId, CollateralLock.ContractId>>> createAndExerciseLock(
      Lock arg) {
    return createAnd().exerciseLock(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseLock} instead
   */
  @Deprecated
  public Update<Exercised<Tuple2<ContractId, CollateralLock.ContractId>>> createAndExerciseLock(
      BigDecimal amount, String reason, Instant expiry) {
    return createAndExerciseLock(new Lock(amount, reason, expiry));
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
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseWithdraw} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseWithdraw(Withdraw arg) {
    return createAnd().exerciseWithdraw(arg);
  }

  /**
   * @deprecated since Daml 2.3.0; use {@code createAnd().exerciseWithdraw} instead
   */
  @Deprecated
  public Update<Exercised<ContractId>> createAndExerciseWithdraw(BigDecimal amount) {
    return createAndExerciseWithdraw(new Withdraw(amount));
  }

  public static Update<Created<ContractId>> create(String custodian, String fundManager,
      String compliance, String auditor, String asset, BigDecimal balance, String accountId) {
    return new CollateralAccount(custodian, fundManager, compliance, auditor, asset, balance,
        accountId).create();
  }

  @Override
  public CreateAnd createAnd() {
    return new CreateAnd(this);
  }

  @Override
  protected ContractCompanion.WithoutKey<Contract, ContractId, CollateralAccount> getCompanion() {
    return COMPANION;
  }

  public static ValueDecoder<CollateralAccount> valueDecoder() throws IllegalArgumentException {
    return ContractCompanion.valueDecoder(COMPANION);
  }

  public DamlRecord toValue() {
    ArrayList<DamlRecord.Field> fields = new ArrayList<DamlRecord.Field>(7);
    fields.add(new DamlRecord.Field("custodian", new Party(this.custodian)));
    fields.add(new DamlRecord.Field("fundManager", new Party(this.fundManager)));
    fields.add(new DamlRecord.Field("compliance", new Party(this.compliance)));
    fields.add(new DamlRecord.Field("auditor", new Party(this.auditor)));
    fields.add(new DamlRecord.Field("asset", new Text(this.asset)));
    fields.add(new DamlRecord.Field("balance", new Numeric(this.balance)));
    fields.add(new DamlRecord.Field("accountId", new Text(this.accountId)));
    return new DamlRecord(fields);
  }

  private static ValueDecoder<CollateralAccount> templateValueDecoder() throws
      IllegalArgumentException {
    return value$ -> {
      Value recordValue$ = value$;
      List<DamlRecord.Field> fields$ = PrimitiveValueDecoders.recordCheck(7,0, recordValue$);
      String custodian = PrimitiveValueDecoders.fromParty.decode(fields$.get(0).getValue());
      String fundManager = PrimitiveValueDecoders.fromParty.decode(fields$.get(1).getValue());
      String compliance = PrimitiveValueDecoders.fromParty.decode(fields$.get(2).getValue());
      String auditor = PrimitiveValueDecoders.fromParty.decode(fields$.get(3).getValue());
      String asset = PrimitiveValueDecoders.fromText.decode(fields$.get(4).getValue());
      BigDecimal balance = PrimitiveValueDecoders.fromNumeric.decode(fields$.get(5).getValue());
      String accountId = PrimitiveValueDecoders.fromText.decode(fields$.get(6).getValue());
      return new CollateralAccount(custodian, fundManager, compliance, auditor, asset, balance,
          accountId);
    } ;
  }

  public static JsonLfDecoder<CollateralAccount> jsonDecoder() {
    return JsonLfDecoders.record(Arrays.asList("custodian", "fundManager", "compliance", "auditor", "asset", "balance", "accountId"), name -> {
          switch (name) {
            case "custodian": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(0, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "fundManager": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(1, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "compliance": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(2, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "auditor": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(3, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.party);
            case "asset": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(4, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            case "balance": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(5, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.numeric(10));
            case "accountId": return com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.JavaArg.at(6, com.daml.ledger.javaapi.data.codegen.json.JsonLfDecoders.text);
            default: return null;
          }
        }
        , (Object[] args) -> new CollateralAccount(JsonLfDecoders.cast(args[0]), JsonLfDecoders.cast(args[1]), JsonLfDecoders.cast(args[2]), JsonLfDecoders.cast(args[3]), JsonLfDecoders.cast(args[4]), JsonLfDecoders.cast(args[5]), JsonLfDecoders.cast(args[6])));
  }

  public static CollateralAccount fromJson(String json) throws JsonLfDecoder.Error {
    return jsonDecoder().decode(new JsonLfReader(json));
  }

  public JsonLfEncoder jsonEncoder() {
    return JsonLfEncoders.record(
        JsonLfEncoders.Field.of("custodian", apply(JsonLfEncoders::party, custodian)),
        JsonLfEncoders.Field.of("fundManager", apply(JsonLfEncoders::party, fundManager)),
        JsonLfEncoders.Field.of("compliance", apply(JsonLfEncoders::party, compliance)),
        JsonLfEncoders.Field.of("auditor", apply(JsonLfEncoders::party, auditor)),
        JsonLfEncoders.Field.of("asset", apply(JsonLfEncoders::text, asset)),
        JsonLfEncoders.Field.of("balance", apply(JsonLfEncoders::numeric, balance)),
        JsonLfEncoders.Field.of("accountId", apply(JsonLfEncoders::text, accountId)));
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
    if (!(object instanceof CollateralAccount)) {
      return false;
    }
    CollateralAccount other = (CollateralAccount) object;
    return Objects.equals(this.custodian, other.custodian) &&
        Objects.equals(this.fundManager, other.fundManager) &&
        Objects.equals(this.compliance, other.compliance) &&
        Objects.equals(this.auditor, other.auditor) && Objects.equals(this.asset, other.asset) &&
        Objects.equals(this.balance, other.balance) &&
        Objects.equals(this.accountId, other.accountId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.custodian, this.fundManager, this.compliance, this.auditor, this.asset,
        this.balance, this.accountId);
  }

  @Override
  public String toString() {
    return String.format("com.canton.etf.model.canton.etf.collateral.collateralaccount.CollateralAccount(%s, %s, %s, %s, %s, %s, %s)",
        this.custodian, this.fundManager, this.compliance, this.auditor, this.asset, this.balance,
        this.accountId);
  }

  public static final class ContractId extends com.daml.ledger.javaapi.data.codegen.ContractId<CollateralAccount> implements Exercises<ExerciseCommand> {
    public ContractId(String contractId) {
      super(contractId);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CollateralAccount, ?> getCompanion(
        ) {
      return COMPANION;
    }

    public static ContractId fromContractId(
        com.daml.ledger.javaapi.data.codegen.ContractId<CollateralAccount> contractId) {
      return COMPANION.toContractId(contractId);
    }
  }

  public static class Contract extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, CollateralAccount> {
    public Contract(ContractId id, CollateralAccount data, Set<String> signatories,
        Set<String> observers) {
      super(id, data, signatories, observers);
    }

    @Override
    protected ContractCompanion<Contract, ContractId, CollateralAccount> getCompanion() {
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
    default Update<Exercised<ContractId>> exerciseDeposit(Deposit arg) {
      return makeExerciseCmd(CHOICE_Deposit, arg);
    }

    default Update<Exercised<ContractId>> exerciseDeposit(BigDecimal amount) {
      return exerciseDeposit(new Deposit(amount));
    }

    default Update<Exercised<Tuple2<ContractId, CollateralLock.ContractId>>> exerciseLock(
        Lock arg) {
      return makeExerciseCmd(CHOICE_Lock, arg);
    }

    default Update<Exercised<Tuple2<ContractId, CollateralLock.ContractId>>> exerciseLock(
        BigDecimal amount, String reason, Instant expiry) {
      return exerciseLock(new Lock(amount, reason, expiry));
    }

    default Update<Exercised<Unit>> exerciseArchive(Archive arg) {
      return makeExerciseCmd(CHOICE_Archive, arg);
    }

    default Update<Exercised<Unit>> exerciseArchive() {
      return exerciseArchive(new Archive());
    }

    default Update<Exercised<ContractId>> exerciseWithdraw(Withdraw arg) {
      return makeExerciseCmd(CHOICE_Withdraw, arg);
    }

    default Update<Exercised<ContractId>> exerciseWithdraw(BigDecimal amount) {
      return exerciseWithdraw(new Withdraw(amount));
    }
  }

  public static final class CreateAnd extends com.daml.ledger.javaapi.data.codegen.CreateAnd implements Exercises<CreateAndExerciseCommand> {
    CreateAnd(Template createArguments) {
      super(createArguments);
    }

    @Override
    protected ContractTypeCompanion<? extends com.daml.ledger.javaapi.data.codegen.Contract<ContractId, ?>, ContractId, CollateralAccount, ?> getCompanion(
        ) {
      return COMPANION;
    }
  }

  /**
   * Proxies the jsonDecoder(...) static method, to provide an alternative calling synatx, which avoids some cases in generated code where javac gets confused
   */
  public static class JsonDecoder$ {
    public JsonLfDecoder<CollateralAccount> get() {
      return jsonDecoder();
    }
  }
}
