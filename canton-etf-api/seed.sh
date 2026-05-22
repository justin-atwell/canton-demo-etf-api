#!/bin/bash
# Canton ETF Platform — Seed Script
# Update FINGERPRINT after each `daml start`

# -------------------------------------------------------------------------
# Party IDs — update FINGERPRINT only
# -------------------------------------------------------------------------
MODE=${1:-all}

echo "⏳ Waiting for API to be ready..."
until curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/etf -H "Authorization: Bearer dev-FundManager" | grep -q "200"; do
  echo "   API not ready, retrying in 2s..."
  sleep 2
done
echo "✅ API is ready"
echo ""

FINGERPRINT="1220fea823c23ecfb9254d5d0ac52656737d5351172d9819033515593aacbd60d17c"

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
# ETFs — 7 total
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
sleep 2

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
sleep 2

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
sleep 2

curl -s -X POST $BASE/etf \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"ticker\": \"QQQ\",
    \"name\": \"Invesco QQQ Trust\",
    \"cusip\": \"46090E103\",
    \"custodian\": \"$CUSTODIAN\",
    \"compliance\": \"$COMPLIANCE\",
    \"auditor\": \"$AUDITOR\"
  }"
echo " ✅ QQQ created"
sleep 2

curl -s -X POST $BASE/etf \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"ticker\": \"IBIT\",
    \"name\": \"iShares Bitcoin Trust ETF\",
    \"cusip\": \"46438F101\",
    \"custodian\": \"$CUSTODIAN\",
    \"compliance\": \"$COMPLIANCE\",
    \"auditor\": \"$AUDITOR\"
  }"
echo " ✅ IBIT created"
sleep 2

curl -s -X POST $BASE/etf \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"ticker\": \"ARKK\",
    \"name\": \"ARK Innovation ETF\",
    \"cusip\": \"00214Q104\",
    \"custodian\": \"$CUSTODIAN\",
    \"compliance\": \"$COMPLIANCE\",
    \"auditor\": \"$AUDITOR\"
  }"
echo " ✅ ARKK created"
sleep 2

curl -s -X POST $BASE/etf \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer dev-FundManager" \
  -d "{
    \"ticker\": \"BITO\",
    \"name\": \"ProShares Bitcoin Strategy ETF\",
    \"cusip\": \"74347B474\",
    \"custodian\": \"$CUSTODIAN\",
    \"compliance\": \"$COMPLIANCE\",
    \"auditor\": \"$AUDITOR\"
  }"
echo " ✅ BITO created"
sleep 5
echo ""

echo "⏳ Waiting for ETF contracts to commit..."
sleep 4
echo ""

# -------------------------------------------------------------------------
# NAV history — all 7 ETFs, 30 days each
# -------------------------------------------------------------------------
if [ "$MODE" = "all" ]; then

  echo "📈 Seeding NAV history for CXBT..."
  for i in {29..0}; do
    DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
    NAV=$(echo "scale=2; 82 + $RANDOM % 10 + $RANDOM % 100 / 100" | bc)
    AUM=$(echo "scale=0; 800000000 + $RANDOM % 100000000" | bc)
    curl -s -X POST $BASE/etf/CXBT/nav \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer dev-FundManager" \
      -d "{\"navDate\":\"$DATE\",\"navPerShare\":$NAV,\"totalAUM\":$AUM,\"source\":\"FundManager\"}"
  done
  echo " ✅ CXBT NAV seeded (30 days)"

  echo "📈 Seeding NAV history for CXETH..."
  for i in {29..0}; do
    DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
    NAV=$(echo "scale=2; 45 + $RANDOM % 8 + $RANDOM % 100 / 100" | bc)
    AUM=$(echo "scale=0; 400000000 + $RANDOM % 50000000" | bc)
    curl -s -X POST $BASE/etf/CXETH/nav \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer dev-FundManager" \
      -d "{\"navDate\":\"$DATE\",\"navPerShare\":$NAV,\"totalAUM\":$AUM,\"source\":\"FundManager\"}"
  done
  echo " ✅ CXETH NAV seeded (30 days)"

  echo "📈 Seeding NAV history for SPY..."
  for i in {29..0}; do
    DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
    NAV=$(echo "scale=2; 520 + $RANDOM % 30 + $RANDOM % 100 / 100" | bc)
    AUM=$(echo "scale=0; 5000000000 + $RANDOM % 500000000" | bc)
    curl -s -X POST $BASE/etf/SPY/nav \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer dev-FundManager" \
      -d "{\"navDate\":\"$DATE\",\"navPerShare\":$NAV,\"totalAUM\":$AUM,\"source\":\"FundManager\"}"
  done
  echo " ✅ SPY NAV seeded (30 days)"

  # QQQ trades ~$480-510 range, AUM ~$250B
  echo "📈 Seeding NAV history for QQQ..."
  for i in {29..0}; do
    DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
    NAV=$(echo "scale=2; 480 + $RANDOM % 30 + $RANDOM % 100 / 100" | bc)
    AUM=$(echo "scale=0; 250000000000 + $RANDOM % 5000000000" | bc)
    curl -s -X POST $BASE/etf/QQQ/nav \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer dev-FundManager" \
      -d "{\"navDate\":\"$DATE\",\"navPerShare\":$NAV,\"totalAUM\":$AUM,\"source\":\"FundManager\"}"
  done
  echo " ✅ QQQ NAV seeded (30 days)"

  # IBIT trades ~$55-65 range, AUM ~$40B
  echo "📈 Seeding NAV history for IBIT..."
  for i in {29..0}; do
    DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
    NAV=$(echo "scale=2; 55 + $RANDOM % 10 + $RANDOM % 100 / 100" | bc)
    AUM=$(echo "scale=0; 40000000000 + $RANDOM % 2000000000" | bc)
    curl -s -X POST $BASE/etf/IBIT/nav \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer dev-FundManager" \
      -d "{\"navDate\":\"$DATE\",\"navPerShare\":$NAV,\"totalAUM\":$AUM,\"source\":\"FundManager\"}"
  done
  echo " ✅ IBIT NAV seeded (30 days)"

  # ARKK trades ~$55-75 range, AUM ~$7B
  echo "📈 Seeding NAV history for ARKK..."
  for i in {29..0}; do
    DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
    NAV=$(echo "scale=2; 55 + $RANDOM % 20 + $RANDOM % 100 / 100" | bc)
    AUM=$(echo "scale=0; 7000000000 + $RANDOM % 500000000" | bc)
    curl -s -X POST $BASE/etf/ARKK/nav \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer dev-FundManager" \
      -d "{\"navDate\":\"$DATE\",\"navPerShare\":$NAV,\"totalAUM\":$AUM,\"source\":\"FundManager\"}"
  done
  echo " ✅ ARKK NAV seeded (30 days)"

  # BITO trades ~$25-35 range, AUM ~$2B
  echo "📈 Seeding NAV history for BITO..."
  for i in {29..0}; do
    DATE=$(date -v -${i}d +%Y-%m-%d 2>/dev/null || date -d "-${i} days" +%Y-%m-%d)
    NAV=$(echo "scale=2; 25 + $RANDOM % 10 + $RANDOM % 100 / 100" | bc)
    AUM=$(echo "scale=0; 2000000000 + $RANDOM % 200000000" | bc)
    curl -s -X POST $BASE/etf/BITO/nav \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer dev-FundManager" \
      -d "{\"navDate\":\"$DATE\",\"navPerShare\":$NAV,\"totalAUM\":$AUM,\"source\":\"FundManager\"}"
  done
  echo " ✅ BITO NAV seeded (30 days)"

fi
echo ""

# -------------------------------------------------------------------------
# Constituents
# -------------------------------------------------------------------------
if [ "$MODE" = "etf-only" ] || [ "$MODE" = "all" ]; then

  # CXBT — broad crypto, weights sum to 1.0
  echo "⚖️  Adding constituents for CXBT..."
  curl -s -X POST $BASE/etf/CXBT/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"BTC","name":"Bitcoin","cusip":"BTC-SPOT","weight":0.45}' > /dev/null && echo " ✅ BTC 45%"
  curl -s -X POST $BASE/etf/CXBT/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"ETH","name":"Ethereum","cusip":"ETH-SPOT","weight":0.25}' > /dev/null && echo " ✅ ETH 25%"
  curl -s -X POST $BASE/etf/CXBT/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"SOL","name":"Solana","cusip":"SOL-SPOT","weight":0.15}' > /dev/null && echo " ✅ SOL 15%"
  curl -s -X POST $BASE/etf/CXBT/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"AVAX","name":"Avalanche","cusip":"AVAX-SPOT","weight":0.10}' > /dev/null && echo " ✅ AVAX 10%"
  curl -s -X POST $BASE/etf/CXBT/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"LINK","name":"Chainlink","cusip":"LINK-SPOT","weight":0.05}' > /dev/null && echo " ✅ LINK 5%"

  # CXETH — ETH ecosystem, weights sum to 1.0
  echo "⚖️  Adding constituents for CXETH..."
  curl -s -X POST $BASE/etf/CXETH/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"ETH","name":"Ethereum","cusip":"ETH-SPOT","weight":0.60}' > /dev/null && echo " ✅ ETH 60%"
  curl -s -X POST $BASE/etf/CXETH/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"LDO","name":"Lido DAO","cusip":"LDO-SPOT","weight":0.20}' > /dev/null && echo " ✅ LDO 20%"
  curl -s -X POST $BASE/etf/CXETH/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"AAVE","name":"Aave","cusip":"AAVE-SPOT","weight":0.20}' > /dev/null && echo " ✅ AAVE 20%"

  # SPY — S&P 500 top holdings, weights sum to 1.0
  echo "⚖️  Adding constituents for SPY..."
  curl -s -X POST $BASE/etf/SPY/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"AAPL","name":"Apple Inc","cusip":"037833100","weight":0.07}' > /dev/null && echo " ✅ AAPL 7%"
  curl -s -X POST $BASE/etf/SPY/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"MSFT","name":"Microsoft Corp","cusip":"594918104","weight":0.07}' > /dev/null && echo " ✅ MSFT 7%"
  curl -s -X POST $BASE/etf/SPY/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"NVDA","name":"NVIDIA Corp","cusip":"67066G104","weight":0.06}' > /dev/null && echo " ✅ NVDA 6%"
  curl -s -X POST $BASE/etf/SPY/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"AMZN","name":"Amazon.com Inc","cusip":"023135106","weight":0.05}' > /dev/null && echo " ✅ AMZN 5%"
  curl -s -X POST $BASE/etf/SPY/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"GOOGL","name":"Alphabet Inc","cusip":"02079K305","weight":0.04}' > /dev/null && echo " ✅ GOOGL 4%"
  curl -s -X POST $BASE/etf/SPY/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"META","name":"Meta Platforms","cusip":"30303M102","weight":0.03}' > /dev/null && echo " ✅ META 3%"
  curl -s -X POST $BASE/etf/SPY/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"BRK","name":"Berkshire Hathaway","cusip":"084670702","weight":0.02}' > /dev/null && echo " ✅ BRK 2%"
  curl -s -X POST $BASE/etf/SPY/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"OTHER","name":"Remaining S&P 500","cusip":"SPY-OTHER","weight":0.66}' > /dev/null && echo " ✅ OTHER 66%"

  # QQQ — Nasdaq-100 top holdings, weights sum to 1.0
  echo "⚖️  Adding constituents for QQQ..."
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"AAPL","name":"Apple Inc","cusip":"037833100","weight":0.09}' > /dev/null && echo " ✅ AAPL 9%"
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"MSFT","name":"Microsoft Corp","cusip":"594918104","weight":0.09}' > /dev/null && echo " ✅ MSFT 9%"
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"NVDA","name":"NVIDIA Corp","cusip":"67066G104","weight":0.08}' > /dev/null && echo " ✅ NVDA 8%"
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"AMZN","name":"Amazon.com Inc","cusip":"023135106","weight":0.06}' > /dev/null && echo " ✅ AMZN 6%"
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"META","name":"Meta Platforms","cusip":"30303M102","weight":0.05}' > /dev/null && echo " ✅ META 5%"
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"GOOGL","name":"Alphabet Inc","cusip":"02079K305","weight":0.05}' > /dev/null && echo " ✅ GOOGL 5%"
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"TSLA","name":"Tesla Inc","cusip":"88160R101","weight":0.04}' > /dev/null && echo " ✅ TSLA 4%"
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"AVGO","name":"Broadcom Inc","cusip":"11135F101","weight":0.04}' > /dev/null && echo " ✅ AVGO 4%"
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"COST","name":"Costco Wholesale","cusip":"22160K105","weight":0.03}' > /dev/null && echo " ✅ COST 3%"
  curl -s -X POST $BASE/etf/QQQ/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"OTHER","name":"Remaining Nasdaq-100","cusip":"QQQ-OTHER","weight":0.47}' > /dev/null && echo " ✅ OTHER 47%"

  # IBIT — pure Bitcoin spot ETF, single constituent
  echo "⚖️  Adding constituents for IBIT..."
  curl -s -X POST $BASE/etf/IBIT/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"BTC","name":"Bitcoin","cusip":"BTC-SPOT","weight":1.00}' > /dev/null && echo " ✅ BTC 100%"

  # ARKK — ARK Innovation, tech/crypto crossover, weights sum to 1.0
  echo "⚖️  Adding constituents for ARKK..."
  curl -s -X POST $BASE/etf/ARKK/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"TSLA","name":"Tesla Inc","cusip":"88160R101","weight":0.12}' > /dev/null && echo " ✅ TSLA 12%"
  curl -s -X POST $BASE/etf/ARKK/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"COIN","name":"Coinbase Global","cusip":"19260Q107","weight":0.11}' > /dev/null && echo " ✅ COIN 11%"
  curl -s -X POST $BASE/etf/ARKK/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"ROKU","name":"Roku Inc","cusip":"77543R102","weight":0.08}' > /dev/null && echo " ✅ ROKU 8%"
  curl -s -X POST $BASE/etf/ARKK/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"HOOD","name":"Robinhood Markets","cusip":"770700102","weight":0.07}' > /dev/null && echo " ✅ HOOD 7%"
  curl -s -X POST $BASE/etf/ARKK/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"SQ","name":"Block Inc","cusip":"852234103","weight":0.07}' > /dev/null && echo " ✅ SQ 7%"
  curl -s -X POST $BASE/etf/ARKK/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"MSTR","name":"MicroStrategy Inc","cusip":"594972408","weight":0.06}' > /dev/null && echo " ✅ MSTR 6%"
  curl -s -X POST $BASE/etf/ARKK/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"OTHER","name":"Remaining ARK Holdings","cusip":"ARKK-OTHER","weight":0.49}' > /dev/null && echo " ✅ OTHER 49%"

  # BITO — Bitcoin futures strategy, weights sum to 1.0
  echo "⚖️  Adding constituents for BITO..."
  curl -s -X POST $BASE/etf/BITO/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"BTC-FUT","name":"Bitcoin CME Futures (Front Month)","cusip":"BTC-FUT-1","weight":0.75}' > /dev/null && echo " ✅ BTC-FUT 75%"
  curl -s -X POST $BASE/etf/BITO/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"BTC-FUT2","name":"Bitcoin CME Futures (Back Month)","cusip":"BTC-FUT-2","weight":0.15}' > /dev/null && echo " ✅ BTC-FUT2 15%"
  curl -s -X POST $BASE/etf/BITO/constituent -H "Content-Type: application/json" -H "Authorization: Bearer dev-FundManager" -d '{"symbol":"TBILL","name":"US Treasury Bills","cusip":"UST-BILL","weight":0.10}' > /dev/null && echo " ✅ TBILL 10%"

fi

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