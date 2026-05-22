#!/bin/bash
# Canton ETF Platform — Reseed Script
# Use this to refresh demo state on DevNet without wiping the ledger.
# Adds fresh collateral locks, margin calls, and rebalance proposals on top
# of existing state. Safe to run multiple times.
# Update FINGERPRINT after each sandbox restart (local only — DevNet fingerprint is stable)

FINGERPRINT="1220e0195503d52c91209b03c5ce259b35cc398c32e92bddaf2ee83cd94a1dceac3e"

FUND_MANAGER="FundManager::${FINGERPRINT}"
CUSTODIAN="Custodian::${FINGERPRINT}"
COMPLIANCE="ComplianceOfficer::${FINGERPRINT}"
AUDITOR="Auditor::${FINGERPRINT}"

BASE="http://localhost:8080"

echo "⏳ Waiting for API to be ready..."
until curl -s -o /dev/null -w "%{http_code}" $BASE/etf \
  -H "Authorization: Bearer dev-FundManager" | grep -q "200"; do
  echo "   API not ready, retrying in 2s..."
  sleep 2
done
echo "✅ API is ready"
echo ""

echo "🔄 Reseeding Canton ETF Platform demo state..."
echo "Fingerprint: $FINGERPRINT"
echo ""

# -------------------------------------------------------------------------
# Timestamp helpers
# -------------------------------------------------------------------------
EXPIRY_7D=$(date -v +7d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+7 days" +%Y-%m-%dT%H:%M:%SZ)
EXPIRY_2D=$(date -v +2d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+2 days" +%Y-%m-%dT%H:%M:%SZ)
EXPIRY_14D=$(date -v +14d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+14 days" +%Y-%m-%dT%H:%M:%SZ)
DUE_3D=$(date -v +3d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+3 days" +%Y-%m-%dT%H:%M:%SZ)
DUE_1D=$(date -v +1d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+1 day" +%Y-%m-%dT%H:%M:%SZ)

# Unique suffix so proposal IDs don't collide with prior runs
SUFFIX=$(date +%s)

# -------------------------------------------------------------------------
# Collateral Locks — fresh locks on existing accounts
# -------------------------------------------------------------------------
echo "🔒 Creating fresh collateral locks..."

curl -s -X POST $BASE/collateral/COLL-CXBT-001/lock \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-CXBT-001\",\"amount\":500000.00,\"reason\":\"Rebalance collateral hold — CXBT constituent adjustment\",\"expiry\":\"$EXPIRY_7D\"}"
echo " ✅ CXBT lock (USD 500,000 — 7 days)"
sleep 1

curl -s -X POST $BASE/collateral/COLL-CXETH-001/lock \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-CXETH-001\",\"amount\":250000.00,\"reason\":\"Pending margin call settlement — COLL-CXETH-001\",\"expiry\":\"$EXPIRY_2D\"}"
echo " ✅ CXETH lock (USD 250,000 — 2 days)"
sleep 1

curl -s -X POST $BASE/collateral/COLL-QQQ-001/lock \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-QQQ-001\",\"amount\":2000000.00,\"reason\":\"Basel III minimum reserve — QQQ regulatory hold\",\"expiry\":\"$EXPIRY_14D\"}"
echo " ✅ QQQ lock (USD 2,000,000 — 14 days)"
sleep 1

curl -s -X POST $BASE/collateral/COLL-IBIT-001/lock \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-IBIT-001\",\"amount\":1000000.00,\"reason\":\"Bitcoin custody reserve — SEC Rule 17a-4 hold\",\"expiry\":\"$EXPIRY_7D\"}"
echo " ✅ IBIT lock (USD 1,000,000 — 7 days)"
echo ""

echo "⏳ Waiting for locks to commit..."
sleep 3

# -------------------------------------------------------------------------
# Margin Calls — fresh calls on existing accounts
# -------------------------------------------------------------------------
echo "📣 Issuing fresh margin calls..."

curl -s -X POST $BASE/collateral/COLL-SPY-001/margincall \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-SPY-001\",\"asset\":\"USD\",\"amountRequired\":250000.00,\"dueBy\":\"$DUE_3D\"}"
echo " ✅ SPY margin call (USD 250,000 — due in 3 days)"
sleep 1

curl -s -X POST $BASE/collateral/COLL-ARKK-001/margincall \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-ARKK-001\",\"asset\":\"USD\",\"amountRequired\":400000.00,\"dueBy\":\"$DUE_1D\"}"
echo " ✅ ARKK margin call (USD 400,000 — due in 1 day)"
echo ""

echo "⏳ Waiting for margin calls to commit..."
sleep 3

# -------------------------------------------------------------------------
# Rebalance Proposals — one per major ETF, unique IDs via timestamp suffix
# Demo flow: FundManager proposes → ComplianceOfficer approves → FundManager executes
# -------------------------------------------------------------------------
echo "⚖️  Posting fresh rebalance proposals..."

# CXBT — shift weight from ETH to SOL
curl -s -X POST $BASE/etf/CXBT/rebalance \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"proposalId\": \"REBAL-CXBT-${SUFFIX}\",
    \"newWeights\": [
      {\"symbol\": \"BTC\",  \"weight\": 0.50},
      {\"symbol\": \"ETH\",  \"weight\": 0.20},
      {\"symbol\": \"SOL\",  \"weight\": 0.18},
      {\"symbol\": \"AVAX\", \"weight\": 0.08},
      {\"symbol\": \"LINK\", \"weight\": 0.04}
    ]
  }"
echo " ✅ CXBT rebalance proposed (BTC 50% / ETH 20% / SOL 18% / AVAX 8% / LINK 4%)"
sleep 1

# CXETH — increase AAVE, reduce LDO
curl -s -X POST $BASE/etf/CXETH/rebalance \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"proposalId\": \"REBAL-CXETH-${SUFFIX}\",
    \"newWeights\": [
      {\"symbol\": \"ETH\",  \"weight\": 0.65},
      {\"symbol\": \"AAVE\", \"weight\": 0.25},
      {\"symbol\": \"LDO\",  \"weight\": 0.10}
    ]
  }"
echo " ✅ CXETH rebalance proposed (ETH 65% / AAVE 25% / LDO 10%)"
sleep 1

# QQQ — increase NVDA weighting
curl -s -X POST $BASE/etf/QQQ/rebalance \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"proposalId\": \"REBAL-QQQ-${SUFFIX}\",
    \"newWeights\": [
      {\"symbol\": \"NVDA\",  \"weight\": 0.12},
      {\"symbol\": \"AAPL\",  \"weight\": 0.09},
      {\"symbol\": \"MSFT\",  \"weight\": 0.09},
      {\"symbol\": \"AMZN\",  \"weight\": 0.06},
      {\"symbol\": \"META\",  \"weight\": 0.05},
      {\"symbol\": \"GOOGL\", \"weight\": 0.05},
      {\"symbol\": \"TSLA\",  \"weight\": 0.04},
      {\"symbol\": \"AVGO\",  \"weight\": 0.03},
      {\"symbol\": \"COST\",  \"weight\": 0.03},
      {\"symbol\": \"OTHER\", \"weight\": 0.44}
    ]
  }"
echo " ✅ QQQ rebalance proposed (NVDA increased to 12%)"
sleep 1

# ARKK — increase COIN, reduce MSTR
curl -s -X POST $BASE/etf/ARKK/rebalance \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"proposalId\": \"REBAL-ARKK-${SUFFIX}\",
    \"newWeights\": [
      {\"symbol\": \"COIN\",  \"weight\": 0.15},
      {\"symbol\": \"TSLA\",  \"weight\": 0.12},
      {\"symbol\": \"ROKU\",  \"weight\": 0.08},
      {\"symbol\": \"HOOD\",  \"weight\": 0.07},
      {\"symbol\": \"SQ\",    \"weight\": 0.07},
      {\"symbol\": \"MSTR\",  \"weight\": 0.02},
      {\"symbol\": \"OTHER\", \"weight\": 0.49}
    ]
  }"
echo " ✅ ARKK rebalance proposed (COIN increased to 15%)"
echo ""

echo "⏳ Waiting for proposals to commit..."
sleep 3

echo "✅ Reseed complete."
echo ""
echo "Demo flow:"
echo "  1. Login as FundManager  → Rebalancing → proposals are Pending"
echo "  2. Switch to Compliance  → Rebalancing → Approve a proposal"
echo "  3. Switch to FundManager → Rebalancing → Execute the approved proposal"
echo "  4. Collateral Monitor    → fresh locks and margin calls visible"
echo "  5. Compliance Panel      → live contract counts updated"
echo "  6. Audit Trail           → all actions recorded on ledger"