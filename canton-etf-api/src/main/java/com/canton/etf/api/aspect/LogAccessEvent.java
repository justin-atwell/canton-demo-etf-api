package com.canton.etf.api.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a service method for automatic AccessEvent recording on the Canton ledger.
 *
 * Usage:
 *   @LogAccessEvent(action = "APPROVE_REBALANCE", resourceParam = "proposalId")
 *   public void approve(String partyId, String ticker, String proposalId) { ... }
 *
 * - action:        The action string written to the AccessEvent contract.
 * - resourceParam: Name of the method parameter to use as the resource identifier.
 *                  If empty, falls back to the method name.
 * - partyParam:    Name of the method parameter holding the actor party ID.
 *                  Defaults to "partyId".
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogAccessEvent {
    String action();
    String resourceParam() default "";
    String partyParam() default "partyId";
}