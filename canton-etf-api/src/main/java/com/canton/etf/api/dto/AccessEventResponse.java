package com.canton.etf.api.dto;

import com.canton.etf.model.canton.etf.iam.accessevent.AccessEvent;

import java.time.Instant;

public record AccessEventResponse(
        String contractId,
        String operator,
        String actor,
        String action,
        String resource,
        Instant timestamp,
        boolean granted,
        String clientIp,
        String sessionId) {

    public static AccessEventResponse from(AccessEvent.Contract contract) {
        return new AccessEventResponse(
                contract.id.contractId,
                contract.data.operator,
                contract.data.actor,
                contract.data.action,
                contract.data.resource,
                contract.data.timestamp,
                contract.data.granted,
                contract.data.clientIp,
                contract.data.sessionId
        );
    }
}