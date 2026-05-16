package com.canton.etf.api.dto;

import com.canton.etf.model.canton.etf.rebalance.rebalanceproposal.RebalanceProposal;
import com.canton.etf.model.da.types.Tuple2;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record RebalanceProposalResponse(
        String contractId,
        String fundManager,
        String custodian,
        String compliance,
        String auditor,
        String ticker,
        String proposalId,
        List<Tuple2<String, BigDecimal>> newWeights,
        Instant proposedAt,
        String status) {

    public static RebalanceProposalResponse from(RebalanceProposal.Contract contract) {
        return new RebalanceProposalResponse(
                contract.id.contractId,
                contract.data.fundManager,
                contract.data.custodian,
                contract.data.compliance,
                contract.data.auditor,
                contract.data.ticker,
                contract.data.proposalId,
                contract.data.newWeights,
                contract.data.proposedAt,
                contract.data.status
        );
    }
}