#!/bin/bash
# Canton ETF Platform — Seed Script
# Update FINGERPRINT after each `daml start`

# -------------------------------------------------------------------------
# Party IDs — update FINGERPRINT only
# -------------------------------------------------------------------------
MODE=${1:-all}

echo "⏳ Waiting for API to be ready..."
until curl -s -o /dev/null -w "%{http_code}" http://34.46.249.56/etf -H "Authorization: Bearer dev-FundManager" | grep -q "200"; do
  echo "   API not ready, retrying in 2s..."
  sleep 2
done
echo "✅ API is ready"
echo ""

FINGERPRINT="122070380ee60609917e12e3ac0703fbc3ea5eb62b2e34ce6b874cb6372a34a52362"

FUND_MANAGER="FundManager::${FINGERPRINT}"
CUSTODIAN="Custodian::${FINGERPRINT}"
COMPLIANCE="ComplianceOfficer::${FINGERPRINT}"
AUDITOR="Auditor::${FINGERPRINT}"
MARKET_MAKER="MarketMaker::${FINGERPRINT}"

BASE="http://34.46.249.56"


# -------------------------------------------------------------------------
# Collateral Accounts — one per ETF
# -------------------------------------------------------------------------
echo "🏦 Creating collateral accounts..."

curl -s -X POST $BASE/collateral \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-CXBT-001\",\"fundManager\":\"$FUND_MANAGER\",\"custodian\":\"$CUSTODIAN\",\"compliance\":\"$COMPLIANCE\",\"auditor\":\"$AUDITOR\",\"asset\":\"USD\",\"initialBalance\":4750000.00}"
echo " ✅ CXBT collateral account (USD 4,750,000)"
sleep 1

curl -s -X POST $BASE/collateral \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-CXETH-001\",\"fundManager\":\"$FUND_MANAGER\",\"custodian\":\"$CUSTODIAN\",\"compliance\":\"$COMPLIANCE\",\"auditor\":\"$AUDITOR\",\"asset\":\"USD\",\"initialBalance\":2100000.00}"
echo " ✅ CXETH collateral account (USD 2,100,000)"
sleep 1

curl -s -X POST $BASE/collateral \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-SPY-001\",\"fundManager\":\"$FUND_MANAGER\",\"custodian\":\"$CUSTODIAN\",\"compliance\":\"$COMPLIANCE\",\"auditor\":\"$AUDITOR\",\"asset\":\"USD\",\"initialBalance\":875000.00}"
echo " ✅ SPY collateral account (USD 875,000)"
sleep 1

curl -s -X POST $BASE/collateral \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-QQQ-001\",\"fundManager\":\"$FUND_MANAGER\",\"custodian\":\"$CUSTODIAN\",\"compliance\":\"$COMPLIANCE\",\"auditor\":\"$AUDITOR\",\"asset\":\"USD\",\"initialBalance\":12500000.00}"
echo " ✅ QQQ collateral account (USD 12,500,000)"
sleep 1

curl -s -X POST $BASE/collateral \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-IBIT-001\",\"fundManager\":\"$FUND_MANAGER\",\"custodian\":\"$CUSTODIAN\",\"compliance\":\"$COMPLIANCE\",\"auditor\":\"$AUDITOR\",\"asset\":\"USD\",\"initialBalance\":8000000.00}"
echo " ✅ IBIT collateral account (USD 8,000,000)"
sleep 1

curl -s -X POST $BASE/collateral \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-ARKK-001\",\"fundManager\":\"$FUND_MANAGER\",\"custodian\":\"$CUSTODIAN\",\"compliance\":\"$COMPLIANCE\",\"auditor\":\"$AUDITOR\",\"asset\":\"USD\",\"initialBalance\":3200000.00}"
echo " ✅ ARKK collateral account (USD 3,200,000)"
sleep 1

curl -s -X POST $BASE/collateral \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{\"accountId\":\"COLL-BITO-001\",\"fundManager\":\"$FUND_MANAGER\",\"custodian\":\"$CUSTODIAN\",\"compliance\":\"$COMPLIANCE\",\"auditor\":\"$AUDITOR\",\"asset\":\"USD\",\"initialBalance\":1500000.00}"
echo " ✅ BITO collateral account (USD 1,500,000)"
echo ""

echo "⏳ Waiting for collateral accounts to commit..."
sleep 3

# -------------------------------------------------------------------------
# Collateral Locks
# -------------------------------------------------------------------------
echo "🔒 Creating collateral locks..."

EXPIRY_7D=$(date -v +7d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+7 days" +%Y-%m-%dT%H:%M:%SZ)
EXPIRY_2D=$(date -v +2d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+2 days" +%Y-%m-%dT%H:%M:%SZ)
EXPIRY_14D=$(date -v +14d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+14 days" +%Y-%m-%dT%H:%M:%SZ)

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
# Margin Calls
# -------------------------------------------------------------------------
echo "📣 Issuing margin calls..."

DUE_3D=$(date -v +3d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+3 days" +%Y-%m-%dT%H:%M:%SZ)
DUE_1D=$(date -v +1d +%Y-%m-%dT%H:%M:%SZ 2>/dev/null || date -d "+1 day" +%Y-%m-%dT%H:%M:%SZ)

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

echo "✅ Seed complete."
echo "   ETFs:              CXBT, CXETH, SPY, QQQ, IBIT, ARKK, BITO"
echo "   Collateral accts:  7 (one per ETF)"
echo "   Locks:             4 active"
echo "   Margin calls:      2 pending"
echo "   Update application.yml with fingerprint: $FINGERPRINT"