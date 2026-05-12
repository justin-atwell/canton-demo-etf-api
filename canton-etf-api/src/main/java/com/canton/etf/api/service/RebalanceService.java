package com.canton.etf.api.service;

import com.canton.etf.common.ledger.LedgerCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RebalanceService {

    private static final Logger log = LoggerFactory.getLogger(RebalanceService.class);
    private final LedgerCommandService ledgerCommandService;

    public RebalanceService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    public String propose(String partyId, String ticker, Object request) {
        log.info("Rebalance proposed for {} by {}", ticker, partyId);
        return "proposal-stub";
    }

    public Object getProposal(String partyId, String ticker, String proposalId) {
        return Map.of("proposalId", proposalId);
    }
    public void approve(String partyId, String ticker, String proposalId) {
        log.info("Rebalance {} approved by {}", proposalId, partyId);
    }

    public void reject(String partyId, String ticker, String proposalId) {
        log.info("Rebalance {} rejected by {}", proposalId, partyId);
    }

    public void execute(String partyId, String ticker, String proposalId) {
        log.info("Rebalance {} executed by {}", proposalId, partyId);
    }
}