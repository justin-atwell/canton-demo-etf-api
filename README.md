# Canton ETF Platform — API

> Java Spring Boot service layer connecting a production-grade ETF tokenization platform
> to the Canton Network ledger via the Daml Java bindings.

![Canton Network](https://img.shields.io/badge/Canton_Network-DevNet-3b82f6?style=flat-square)
![Daml SDK](https://img.shields.io/badge/Daml_SDK-3.4.11-10b981?style=flat-square)
![Java](https://img.shields.io/badge/Java-21-f59e0b?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-6db33f?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-64748b?style=flat-square)

---

## What This Is

The API layer of the Canton ETF Platform — a four-repo system modeling how a regulated
ETF would actually operate on a distributed ledger.

This module is the bridge between the React frontend and the Canton Network ledger.
Every REST call that mutates state submits a Daml ledger command via gRPC. Every read
queries the Active Contract Set filtered by party identity. No middleware. No mock
transactions. Real ledger state.

The platform now includes a full **Prime Brokerage** module — collateral pools,
margin calls, and an atomic liquidation waterfall — modeling the multi-party
authorization patterns that arise when a PrimeBroker, HedgeFund, Custodian, and
RiskManager all hold different rights over the same underlying collateral.

---

## Architecture

canton-demo-etf-ui/      — React + TypeScript frontend
canton-demo-etf-api/     ← You are here
canton-demo-etf-daml/    — Daml contracts
canton-demo-etf-infra/   — Terraform + Helm + GKE Autopilot

### Module Structure

canton-etf-common/     — Shared ledger infrastructure
LedgerCommandService   — gRPC command submission + ACS queries
LedgerClientConfig     — ManagedChannel wiring (Canton SDK 3.4.x)
Auth0JwtValidator      — JWT validation against Auth0 JWKS endpoint
CantonPartyResolver    — Party ID resolution from JWT claims
canton-etf-api/        — REST API gateway (port 8080)
EtfController          — ETF lifecycle endpoints
RebalanceController    — Multi-party rebalance workflow
CollateralController   — Collateral account + lock management
NAVController          — NAV history
AuditController        — AccessEvent feed
CollateralPoolController       — Prime brokerage collateral pools
CollateralEligibilityController — Collateral eligibility rules
SubstitutionRequestController  — Three-party collateral substitution
MarginCallV2Controller          — Margin call lifecycle
LiquidationWaterfallController  — Priority-ordered liquidation
canton-etf-iam/        — LDAP → Canton IAM bridge (port 8081)
LdapSyncService        — Syncs LDAP users to DirectoryEntry contracts
RoleMembershipService  — Manages RoleMembership as ledger objects
canton-etf-fix/        — QuickFIX/J + Polygon.io NBBO oracle (port 8082)
NbboOracleService      — Posts NBBOQuote contracts from live market data

---

## Ledger Communication

### The Core Pattern

All ledger interaction flows through `LedgerCommandService`:

**Writes — single-party `submitAndWait`**
```java
public String submitAndWait(String partyId, String applicationId, List<Command> commands)
```
Submits a Daml command authorized by a single party.

**Writes — multi-party `submitAndWait`**
```java
public String submitAndWait(List<String> partyIds, String applicationId, List<Command> commands)
public String submitAndWait(List<String> actAs, List<String> readAs, String applicationId, List<Command> commands)
```
Two distinct multi-party patterns, used for two distinct problems:

- **`actAs`-only** — when a choice's authorization requires more than one signatory
  (e.g. creating a `CollateralPool`, which has signatories `hedgeFund, custodian`),
  or when a choice's `do` block `fetch`es, `archive`s, or `create`s a *different*
  contract whose full signatory set must be represented among the transaction's
  authorizers — not just visible to it.
- **`actAs` + `readAs`** — when a party needs visibility into a contract it is not
  itself authorizing anything on (e.g. an observer who needs to look up a related
  contract's data without being a required signer of the action).

These are not interchangeable. A `fetch`/`archive`/`create` inside a Daml choice
body requires its target's *stakeholders* to be among the *authorizers* of that
specific node — `readAs` alone grants visibility but does not satisfy this
authorization check. This distinction cost real debugging time during Prime
Brokerage development (see `LiquidationWaterfall` below) and is documented here
so it isn't rediscovered the hard way twice.

**Reads — `getActiveContracts`**
```java
public List<CreatedEvent> getActiveContracts(String partyId, EventFormat eventFormat)
```
Queries the Active Contract Set via `StateServiceGrpc`, filtered by party identity
(Canton's privacy enforcement) and template. A party only sees contracts where it
is a signatory or observer — this is enforced at the ledger level, not the API
level, and several services in this codebase learned that the hard way (e.g. a
PrimeBroker calling a `CollateralPool` endpoint will reliably get "not found,"
not because the contract doesn't exist, but because PrimeBroker has no stake in it).

### Why This Is Non-Trivial

Canton SDK 3.4.x changed the ACS query API significantly from earlier versions.
`TransactionFilter` was removed as a standalone class. `GetActiveContractsRequest`
moved from a protobuf builder pattern to a Java bindings constructor. `EventFormat`
replaced the party-filter wrapping entirely. None of this is accurately reflected
in the existing documentation — the actual API surface was discovered by inspecting
the compiled SDK classes directly.

---

## Daml Contract Inventory

**IAM Layer**
- `DirectoryEntry` — Canton party ↔ LDAP DN mapping. Signatories: operator + party.
- `RoleMembership` — Cryptographic role grant. Not a DB row — a signed ledger contract.
- `AccessEvent` — Immutable audit record. Append-only, no choices.

**Fund Layer**
- `ETFDefinition` — The fund itself. Signatories: FundManager + Custodian + Compliance.
- `Constituent` — Individual holding with target weight.
- `CapTable` — Ownership distribution.
- `NAV` — Immutable daily NAV record. No choices.

**Collateral Layer**
- `CollateralAccount` — Custodian-controlled asset account.
- `CollateralLock` — Encumbers a specific amount for a specific reason.
- `CollateralRelease` — Unlocks previously locked collateral.
- `HaircutSchedule` — Risk-adjusted collateral valuation.
- `MarginCall` — Issued by Custodian, met or defaulted by FundManager.
- `LiquidationOrder` — Triggered on MarginCall default.

**Rebalance Layer**
- `RebalanceProposal` — Proposed by FundManager. Requires ComplianceOfficer approval.
- `ConstituentApproval` — Per-constituent sign-off.
- `RebalanceExecution` — Final execution record post-approval.

**Market Data**
- `NBBOQuote` — National Best Bid/Offer posted by MarketMaker via QuickFIX/J.

**Prime Brokerage Layer**
- `CollateralPool` — Source of truth for posted collateral. Signatories: `hedgeFund,
  custodian`. Observer: `riskManager`. Revaluation (`RevaluePool`) is
  custodian-controlled; position management (`AddPosition`, `RemovePosition`) is
  hedge-fund-controlled.
- `CollateralEligibility` — Eligibility rules and haircut schedule for a given
  asset class.
- `SubstitutionRequest` — Three-party collateral substitution workflow
  (`PENDING → APPROVED_BY_BROKER → COMPLETED`). PrimeBroker approval requires
  `readAs` visibility into a HedgeFund-signed contract.
- `MarginCallV2` — Margin call lifecycle with a response deadline and automatic
  default. Sole signatory: `primeBroker`. `RespondToCall` (hedge-fund-controlled),
  `SatisfyCall`, `DeclareDefault`, and `UpdateCoverage` (all prime-broker-controlled)
  drive the state machine `Issued → ResponseReceived → Satisfied | Defaulted`.
- `LiquidationWaterfall` — Priority-ordered liquidation of a `CollateralPool`
  against a defaulted `MarginCallV2`. Signatories on `ExecuteWaterfall`:
  `primeBroker, hedgeFund, custodian` — the latter two are required not because
  they control the choice's business logic, but because the choice body fetches,
  archives, and re-creates the underlying `CollateralPool`, whose own signatories
  (`hedgeFund, custodian`) must be authorizers of any transaction that touches it.
  Liquidates by fixed priority (MMF → Treasury → BTC) until the shortfall is
  covered or collateral is exhausted; produces one `LiquidationAuditEvent` per
  position liquidated.
- `LiquidationAuditEvent` — Immutable, append-only audit record of a single
  liquidation step. Signatory: `primeBroker`. Observers: `hedgeFund, riskManager,
  custodian`.

---

## Multi-Party Submit

Some Daml choices require multiple signatories, and some require authorizers
beyond the choice's own controller because of what happens *inside* the choice
body. Two examples from this codebase:

**Direct multi-signatory creation** — `CollateralPool` has signatories
`hedgeFund, custodian`. Creating one requires both:

```java
ledgerCommandService.submitAndWait(
    List.of(hedgeFundPartyId, custodianPartyId),
    applicationId,
    commands
);
```

**Indirect authorization via a nested fetch/archive/create** — `LiquidationWaterfall.
ExecuteWaterfall` is conceptually a PrimeBroker action (they're the one liquidating
a defaulted hedge fund's collateral), but its `do` block `fetch`es, `archive`s, and
`create`s a `CollateralPool`. Daml requires every stakeholder of a contract touched
this way to be an authorizer of the action — so the choice's controller list is
`primeBroker, hedgeFund, custodian`, and the Java submission must include all three
in `actAs`:

```java
ledgerCommandService.submitAndWait(
    List.of(primeBrokerPartyId, hedgeFundPartyId, custodianPartyId),
    applicationId,
    commands
);
```

This is enforced at the ledger layer, not just a UI convention. Get the authorizer
set wrong and the ledger rejects the transaction with a precise but easy-to-misread
`DAML_AUTHORIZATION_ERROR` naming exactly which stakeholders were missing.

---

## Running Locally

**Prerequisites**
- Java 21 (`JAVA_HOME=/opt/homebrew/opt/openjdk@21`)
- Daml SDK 3.4.11
- Canton sandbox running on `localhost:6865`

**Start the Canton sandbox**
```bash
cd ../canton-demo-etf-daml
daml sandbox
```

**Upload the DAR and allocate parties**
```bash
daml ledger upload-dar --host localhost --port 6865 \
  .daml/dist/canton-demo-etf-0.1.0.dar

daml ledger allocate-parties --host localhost --port 6865 \
  FundManager Custodian ComplianceOfficer Auditor MarketMaker \
  HedgeFund PrimeBroker RiskManager
```

Every fresh sandbox session generates new party fingerprints. Copy the allocated
party ID for each role into `Auth0JwtValidator.getDevPartyId()` before starting
the API — stale fingerprints surface as `CONTRACT_NOT_FOUND` errors that look
unrelated to the actual cause.

**Build and run**
```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@21 gradle build
gradle :canton-etf-api:bootRun
```

**Run unit tests**
```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@21 gradle :canton-etf-common:test
```

**Run integration tests** (requires sandbox running)
```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@21 gradle :canton-etf-common:test -Dintegration=true
```

---

## Current Status

| Component | Status |
|-----------|--------|
| `LedgerCommandService` — command submission | ✅ Complete |
| `LedgerCommandService` — ACS queries | ✅ Complete |
| Canton sandbox connectivity | ✅ Verified |
| `EtfService` — ledger wiring | ✅ Complete |
| `RebalanceService` — ledger wiring | ✅ Complete |
| `CollateralService` — ledger wiring | ✅ Complete |
| `NAVService` — ledger wiring | ✅ Complete |
| `CollateralPoolService` — create, position mgmt, revalue | ✅ Complete, curl-verified |
| `CollateralEligibilityService` | ✅ Complete |
| `SubstitutionRequestService` — three-party lifecycle | ✅ Complete, curl-verified |
| `MarginCallV2Service` — full lifecycle incl. default | ✅ Complete, curl-verified |
| `LiquidationWaterfallService` — atomic execution | ✅ Complete, curl-verified |
| Unit tests | ✅ Passing |
| Integration tests | ✅ Passing against local sandbox |
| Frontend wired to live prime brokerage endpoints | 🔜 In progress |
| DevNet deployment | ✅ Live |
| GKE production deployment | ✅ Live |

---

## About

Built by **Justin Atwell** — Principal Solutions Architect with 15 years across
institutional capital markets (Edward Jones FIX/T+1, Bridgewater Associates)
and enterprise DLT deployments (Hedera Hashgraph / Avery Dennison atma.io —
one of the largest production DLT deployments globally).

Part of an ongoing LinkedIn series on tokenized asset infrastructure on Canton Network.

Follow along: [linkedin.com/in/justin-atwell](https://linkedin.com/in/justin-atwell)

---

*Canton ETF Platform is a demonstration system built for educational and portfolio purposes.*