#!/bin/bash
# Canton ETF Platform — Seed Script
# Update FINGERPRINT after each `daml start`

# -------------------------------------------------------------------------
# Party IDs — update FINGERPRINT only
# -------------------------------------------------------------------------
FINGERPRINT="1220a55eafce442d53c42f69299ba0ad363197e8fac131f2a80ed1e5de8b23b23e27"

FUND_MANAGER="FundManager::${FINGERPRINT}"
CUSTODIAN="Custodian::${FINGERPRINT}"
COMPLIANCE="ComplianceOfficer::${FINGERPRINT}"
AUDITOR="Auditor::${FINGERPRINT}"
MARKET_MAKER="MarketMaker::${FINGERPRINT}"

BASE="http://localhost:8080"

echo "🚀 Seeding Canton ETF Platform..."
echo "Fingerprint: $FINGERPRINT"
echo ""

# -------------------------------------------------------------------------
# ETFs
# -------------------------------------------------------------------------
echo "📋 Creating ETFs..."

curl -s -X POST $BASE/etf \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"ticker\": \"CXBT\",
    \"name\": \"Canton Digital Asset ETF\",
    \"cusip\": \"14314X100\",
    \"custodian\": \"$CUSTODIAN\",
    \"compliance\": \"$COMPLIANCE\",
    \"auditor\": \"$AUDITOR\"
  }"
echo " ✅ CXBT created"

curl -s -X POST $BASE/etf \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"ticker\": \"CXETH\",
    \"name\": \"Canton Ethereum ETF\",
    \"cusip\": \"14314X200\",
    \"custodian\": \"$CUSTODIAN\",
    \"compliance\": \"$COMPLIANCE\",
    \"auditor\": \"$AUDITOR\"
  }"
echo " ✅ CXETH created"

curl -s -X POST $BASE/etf \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"ticker\": \"SPY\",
    \"name\": \"SPDR S&P 500 ETF Trust\",
    \"cusip\": \"78462F103\",
    \"custodian\": \"$CUSTODIAN\",
    \"compliance\": \"$COMPLIANCE\",
    \"auditor\": \"$AUDITOR\"
  }"
echo " ✅ SPY created"

echo ""

# -------------------------------------------------------------------------
# NAV — CXBT
# -------------------------------------------------------------------------
echo "📈 Seeding NAV history for CXBT..."

for i in {29..0}; do
  DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
  NAV=$(echo "scale=2; 82 + $RANDOM % 10 + $RANDOM % 100 / 100" | bc)
  AUM=$(echo "scale=0; 800000000 + $RANDOM % 100000000" | bc)
  curl -s -X POST $BASE/etf/CXBT/nav \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer dev-FundManager" \
    -d "{
      \"navDate\": \"$DATE\",
      \"navPerShare\": $NAV,
      \"totalAUM\": $AUM,
      \"source\": \"FundManager\"
    }" > /dev/null
done
echo " ✅ CXBT NAV seeded (30 days)"

# -------------------------------------------------------------------------
# NAV — CXETH
# -------------------------------------------------------------------------
echo "📈 Seeding NAV history for CXETH..."

for i in {29..0}; do
  DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
  NAV=$(echo "scale=2; 45 + $RANDOM % 8 + $RANDOM % 100 / 100" | bc)
  AUM=$(echo "scale=0; 400000000 + $RANDOM % 50000000" | bc)
  curl -s -X POST $BASE/etf/CXETH/nav \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer dev-FundManager" \
    -d "{
      \"navDate\": \"$DATE\",
      \"navPerShare\": $NAV,
      \"totalAUM\": $AUM,
      \"source\": \"FundManager\"
    }" > /dev/null
done
echo " ✅ CXETH NAV seeded (30 days)"

# -------------------------------------------------------------------------
# NAV — SPY
# -------------------------------------------------------------------------
echo "📈 Seeding NAV history for SPY..."

for i in {29..0}; do
  DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
  NAV=$(echo "scale=2; 520 + $RANDOM % 30 + $RANDOM % 100 / 100" | bc)
  AUM=$(echo "scale=0; 5000000000 + $RANDOM % 500000000" | bc)
  curl -s -X POST $BASE/etf/SPY/nav \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer dev-FundManager" \
    -d "{
      \"navDate\": \"$DATE\",
      \"navPerShare\": $NAV,
      \"totalAUM\": $AUM,
      \"source\": \"FundManager\"
    }" > /dev/null
done
echo " ✅ SPY NAV seeded (30 days)"

echo ""

# -------------------------------------------------------------------------
# Constituents — CXBT (weights sum to 1.0)
# -------------------------------------------------------------------------
echo "⚖️  Adding constituents for CXBT..."

curl -s -X POST $BASE/etf/CXBT/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"BTC","name":"Bitcoin","cusip":"BTC-SPOT","weight":0.45}' > /dev/null
echo " ✅ BTC 45%"

curl -s -X POST $BASE/etf/CXBT/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"ETH","name":"Ethereum","cusip":"ETH-SPOT","weight":0.25}' > /dev/null
echo " ✅ ETH 25%"

curl -s -X POST $BASE/etf/CXBT/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"SOL","name":"Solana","cusip":"SOL-SPOT","weight":0.15}' > /dev/null
echo " ✅ SOL 15%"

curl -s -X POST $BASE/etf/CXBT/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"AVAX","name":"Avalanche","cusip":"AVAX-SPOT","weight":0.10}' > /dev/null
echo " ✅ AVAX 10%"

curl -s -X POST $BASE/etf/CXBT/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"LINK","name":"Chainlink","cusip":"LINK-SPOT","weight":0.05}' > /dev/null
echo " ✅ LINK 5%"

# -------------------------------------------------------------------------
# Constituents — CXETH (weights sum to 1.0)
# -------------------------------------------------------------------------
echo "⚖️  Adding constituents for CXETH..."

curl -s -X POST $BASE/etf/CXETH/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"ETH","name":"Ethereum","cusip":"ETH-SPOT","weight":0.60}' > /dev/null
echo " ✅ ETH 60%"

curl -s -X POST $BASE/etf/CXETH/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"LDO","name":"Lido DAO","cusip":"LDO-SPOT","weight":0.20}' > /dev/null
echo " ✅ LDO 20%"

curl -s -X POST $BASE/etf/CXETH/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"AAVE","name":"Aave","cusip":"AAVE-SPOT","weight":0.20}' > /dev/null
echo " ✅ AAVE 20%"

# -------------------------------------------------------------------------
# Constituents — SPY (weights sum to 1.0)
# -------------------------------------------------------------------------
echo "⚖️  Adding constituents for SPY..."

curl -s -X POST $BASE/etf/SPY/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"AAPL","name":"Apple Inc","cusip":"037833100","weight":0.07}' > /dev/null
echo " ✅ AAPL 7%"

curl -s -X POST $BASE/etf/SPY/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"MSFT","name":"Microsoft Corp","cusip":"594918104","weight":0.07}' > /dev/null
echo " ✅ MSFT 7%"

curl -s -X POST $BASE/etf/SPY/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"NVDA","name":"NVIDIA Corp","cusip":"67066G104","weight":0.06}' > /dev/null
echo " ✅ NVDA 6%"

curl -s -X POST $BASE/etf/SPY/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"AMZN","name":"Amazon.com Inc","cusip":"023135106","weight":0.05}' > /dev/null
echo " ✅ AMZN 5%"

curl -s -X POST $BASE/etf/SPY/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"GOOGL","name":"Alphabet Inc","cusip":"02079K305","weight":0.04}' > /dev/null
echo " ✅ GOOGL 4%"

curl -s -X POST $BASE/etf/SPY/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"META","name":"Meta Platforms","cusip":"30303M102","weight":0.03}' > /dev/null
echo " ✅ META 3%"

curl -s -X POST $BASE/etf/SPY/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"BRK","name":"Berkshire Hathaway","cusip":"084670702","weight":0.02}' > /dev/null
echo " ✅ BRK 2%"

curl -s -X POST $BASE/etf/SPY/constituent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d '{"symbol":"OTHER","name":"Remaining S&P 500","cusip":"SPY-OTHER","weight":0.66}' > /dev/null
echo " ✅ OTHER 66%"

# -------------------------------------------------------------------------
# Collateral Accounts
# -------------------------------------------------------------------------
echo "🏦 Creating collateral accounts..."

curl -s -X POST $BASE/collateral/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{
    \"accountId\": \"COLL-CXBT-001\",
    \"owner\": \"$FUND_MANAGER\",
    \"custodian\": \"$CUSTODIAN\",
    \"compliance\": \"$COMPLIANCE\",
    \"auditor\": \"$AUDITOR\",
    \"assetType\": \"BTC\",
    \"balance\": 1500000.00
  }" > /dev/null
echo " ✅ CXBT collateral account created"

curl -s -X POST $BASE/collateral/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-Custodian" \
  -d "{
    \"accountId\": \"COLL-CXETH-001\",
    \"owner\": \"$FUND_MANAGER\",
    \"custodian\": \"$CUSTODIAN\",
    \"compliance\": \"$COMPLIANCE\",
    \"auditor\": \"$AUDITOR\",
    \"assetType\": \"ETH\",
    \"balance\": 750000.00
  }" > /dev/null
echo " ✅ CXETH collateral account created"

echo ""
echo "✅ Seed complete. Update application.yml with fingerprint: $FINGERPRINT"