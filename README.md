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

---

## Architecture

canton-demo-etf-ui/      — React + TypeScript frontend
canton-demo-etf-api/     ← You are here
canton-demo-etf-daml/    — 16 Daml contracts
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
canton-etf-iam/        — LDAP → Canton IAM bridge (port 8081)
LdapSyncService        — Syncs LDAP users to DirectoryEntry contracts
RoleMembershipService  — Manages RoleMembership as ledger objects
canton-etf-fix/        — QuickFIX/J + Polygon.io NBBO oracle (port 8082)
NbboOracleService      — Posts NBBOQuote contracts from live market data

---

## Ledger Communication

### The Core Pattern

All ledger interaction flows through two methods in `LedgerCommandService`:

**Writes — `submitAndWait`**
```java
public String submitAndWait(String partyId, String applicationId, List<Command> commands)
```
Submits a Daml command to the ledger and blocks until the transaction is committed.
Returns the `commandId` for correlation. Every contract creation and choice exercise
goes through this method.

**Reads — `getActiveContracts`**
```java
public List<CreatedEvent> getActiveContracts(String partyId, EventFormat eventFormat)
```
Queries the Active Contract Set via `StateServiceGrpc`. The `EventFormat` encodes
both the party filter (Canton's privacy enforcement) and the template filter
(which contract type to return). Results are streamed from the ledger and
accumulated into typed `CreatedEvent` objects via the Daml Java bindings.

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

---

## Multi-Party Submit

Some Daml choices require multiple signatories. For example `ETFDefinition.Suspend`
requires both `fundManager` and `compliance` to authorize. The SDK 3.4.x pattern:

```java
var submission = CommandsSubmission.create(applicationId, commandId, Optional.empty(), commands)
    .withActAs(fundManagerPartyId)
    .withActAs(compliancePartyId);
```

This is enforced at the ledger layer — not just a UI convention. If either party's
signature is missing the transaction is rejected.

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
  FundManager Custodian ComplianceOfficer Auditor MarketMaker
```

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
| Unit tests | ✅ Passing |
| Integration tests | ✅ Passing against local sandbox |
| `EtfService` — ledger wiring | 🔜 In progress |
| `RebalanceService` — ledger wiring | 🔜 In progress |
| `CollateralService` — ledger wiring | 🔜 In progress |
| `NAVService` — ledger wiring | 🔜 In progress |
| DevNet deployment | 🔜 Pending IP whitelist |
| GKE production deployment | 🔜 Pending |

---

## About

Built by **Justin Atwell** — Principal Solutions Architect with 15 years across
institutional capital markets (Edward Jones FIX/T+1, Bridgewater Associates)
and enterprise DLT deployments (Hedera Hashgraph / Avery Dennison atma.io —
one of the largest production DLT deployments globally).

Part of a five-part LinkedIn series on tokenized asset infrastructure on Canton Network.

Follow along: [linkedin.com/in/justin-atwell](https://linkedin.com/in/justin-atwell)

---

*Canton ETF Platform is a demonstration system built for educational and portfolio purposes.*