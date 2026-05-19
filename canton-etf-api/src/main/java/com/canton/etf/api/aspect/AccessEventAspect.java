package com.canton.etf.api.aspect;

import com.canton.etf.api.service.AccessEventService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Aspect
@Component
public class AccessEventAspect {

    private final AccessEventService accessEventService;

    public AccessEventAspect(AccessEventService accessEventService) {
        this.accessEventService = accessEventService;
    }

    @Around("@annotation(logAccessEvent)")
    public Object intercept(ProceedingJoinPoint joinPoint, LogAccessEvent logAccessEvent) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        Object[] args = joinPoint.getArgs();

        String partyId = extractParam(parameters, args, logAccessEvent.partyParam(), "partyId");
        String resource = logAccessEvent.resourceParam().isEmpty()
                ? signature.getMethod().getName()
                : extractParam(parameters, args, logAccessEvent.resourceParam(), signature.getMethod().getName());

        boolean granted = true;
        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable t) {
            granted = false;
            throw t;
        } finally {
            // Record access event regardless of success or failure
            // Never blocks — AccessEventService swallows its own exceptions
            if (partyId != null) {
                accessEventService.record(partyId, logAccessEvent.action(), resource, granted);
            } else {
                log.warn("AccessEventAspect: could not resolve partyId for method={}",
                        signature.getMethod().getName());
            }
        }
    }

    private String extractParam(Parameter[] parameters, Object[] args, String name, String fallback) {
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals(name)) {
                return args[i] != null ? args[i].toString() : fallback;
            }
        }
        return fallback;
    }
}