package com.canton.etf.api.service;

import com.canton.etf.api.dto.CollateralAccountResponse;
import com.canton.etf.api.dto.CollateralLockResponse;
import com.canton.etf.api.dto.CollateralTransactionRequest;
import com.canton.etf.api.dto.CreateCollateralAccountRequest;
import com.canton.etf.api.dto.LockCollateralRequest;
import com.canton.etf.api.dto.MarginCallRequest;
import com.canton.etf.api.dto.MarginCallResponse;
import com.canton.etf.common.ledger.LedgerCommandService;
import com.canton.etf.model.canton.etf.collateral.collateralaccount.CollateralAccount;
import com.canton.etf.model.canton.etf.collateral.collaterallock.CollateralLock;
import com.canton.etf.model.canton.etf.collateral.margincall.MarginCall;
import com.daml.ledger.javaapi.data.Command;
import com.daml.ledger.javaapi.data.CreatedEvent;
import com.daml.ledger.javaapi.data.CumulativeFilter;
import com.daml.ledger.javaapi.data.EventFormat;
import com.daml.ledger.javaapi.data.Filter;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.canton.etf.common.ledger.LedgerCommandService.log;

@Service
public class CollateralService {

    private static final String APP_ID = "canton-etf-api";

    private final LedgerCommandService ledgerCommandService;

    public CollateralService(LedgerCommandService ledgerCommandService) {
        this.ledgerCommandService = ledgerCommandService;
    }

    // -------------------------------------------------------------------------
    // createAccount
    //   Signatory: custodian (partyId)
    //   All parties supplied in request — no ETF lookup needed.
    // -------------------------------------------------------------------------
    public String createAccount(String partyId, CreateCollateralAccountRequest request) {
        var command = new CollateralAccount(
                partyId,                                        // custodian
                request.fundManager(),
                request.compliance(),
                request.auditor(),
                request.asset(),
                BigDecimal.valueOf(request.initialBalance()),
                request.accountId()
        ).create().commands().get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));

        log.info("CollateralAccount created: accountId={} asset={}", request.accountId(), request.asset());
        return request.accountId();
    }

    // -------------------------------------------------------------------------
    // deposit
    //   Exercises Deposit choice — increases balance.
    //   Controller: custodian
    // -------------------------------------------------------------------------
    public void deposit(String accountId, String partyId, CollateralTransactionRequest request) {
        CreatedEvent event = findAccountEvent(partyId, accountId)
                .orElseThrow(() -> new RuntimeException(
                        "CollateralAccount not found for deposit: " + accountId));

        Command command = new CollateralAccount.ContractId(event.getContractId())
                .exerciseDeposit(BigDecimal.valueOf(request.amount()))
                .commands()
                .get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("Deposit exercised: accountId={} amount={}", accountId, request.amount());
    }

    // -------------------------------------------------------------------------
    // lock
    //   Exercises Lock choice — reduces balance, creates CollateralLock.
    //   Controller: custodian
    //   accountId carried inside LockCollateralRequest.
    // -------------------------------------------------------------------------
    public void lock(String partyId, LockCollateralRequest request) {
        CreatedEvent event = findAccountEvent(partyId, request.accountId())
                .orElseThrow(() -> new RuntimeException(
                        "CollateralAccount not found for lock: " + request.accountId()));

        Command command = new CollateralAccount.ContractId(event.getContractId())
                .exerciseLock(
                        BigDecimal.valueOf(request.amount()),
                        request.reason(),
                        Instant.parse(request.expiry())
                )
                .commands()
                .get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("Lock exercised: accountId={} amount={} reason={}",
                request.accountId(), request.amount(), request.reason());
    }

    // -------------------------------------------------------------------------
    // withdraw
    //   Exercises Withdraw choice — reduces balance.
    //   Controller: custodian
    //   accountId carried inside CollateralTransactionRequest.
    // -------------------------------------------------------------------------
    public void withdraw(String partyId, String accountId, CollateralTransactionRequest request) {
        CreatedEvent event = findAccountEvent(partyId, accountId)
                .orElseThrow(() -> new RuntimeException(
                        "CollateralAccount not found for withdraw: " + accountId));

        Command command = new CollateralAccount.ContractId(event.getContractId())
                .exerciseWithdraw(BigDecimal.valueOf(request.amount()))
                .commands()
                .get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("Withdraw exercised: accountId={} amount={}", accountId, request.amount());
    }

    // -------------------------------------------------------------------------
    // getLocks
    //   Returns all active CollateralLock contracts visible to the party.
    // -------------------------------------------------------------------------
    public List<CollateralLockResponse> getLocks(String partyId) {
        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("CollateralLock"))
                .map(CollateralLock.Contract::fromCreatedEvent)
                .map(CollateralLockResponse::from)
                .toList();
    }

    // -------------------------------------------------------------------------
    // issueMarginCall
    //   Creates a MarginCall contract.
    //   Signatory: custodian (partyId)
    //   Parties resolved from existing CollateralAccount by accountId.
    // -------------------------------------------------------------------------
    public void issueMarginCall(String partyId, MarginCallRequest request) {
        CreatedEvent event = findAccountEvent(partyId, request.accountId())
                .orElseThrow(() -> new RuntimeException(
                        "CollateralAccount not found for margin call: " + request.accountId()));

        CollateralAccount.Contract account = CollateralAccount.Contract.fromCreatedEvent(event);

        var command = new MarginCall(
                partyId,                                        // custodian
                account.data.fundManager,
                account.data.compliance,
                account.data.auditor,
                request.asset(),
                BigDecimal.valueOf(request.amountRequired()),
                Instant.now(),                                  // issuedAt
                Instant.parse(request.dueBy()),
                "Pending"
        ).create().commands().get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("MarginCall issued: accountId={} amountRequired={}", request.accountId(), request.amountRequired());
    }

    // -------------------------------------------------------------------------
    // getMarginCalls
    // -------------------------------------------------------------------------
    public List<MarginCallResponse> getMarginCalls(String partyId) {
        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("MarginCall"))
                .map(MarginCall.Contract::fromCreatedEvent)
                .map(MarginCallResponse::from)
                .toList();
    }

    // -------------------------------------------------------------------------
    // meetMarginCall
    //   Exercises Meet choice — transitions status to "Met".
    //   Controller: fundManager
    // -------------------------------------------------------------------------
    public void meetMarginCall(String partyId, String callId, MarginCallRequest request) {
        Command command = new MarginCall.ContractId(callId)
                .exerciseMeet()
                .commands()
                .get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("MarginCall met: contractId={}", callId);
    }

    // -------------------------------------------------------------------------
    // defaultMarginCall
    //   Exercises Default choice — transitions status to "Defaulted".
    //   Controller: custodian
    // -------------------------------------------------------------------------
    public void defaultMarginCall(String partyId, String callId, MarginCallRequest request) {
        Command command = new MarginCall.ContractId(callId)
                .exerciseDefault()
                .commands()
                .get(0);

        ledgerCommandService.submitAndWait(partyId, APP_ID, List.of(command));
        log.info("MarginCall defaulted: contractId={}", callId);
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private Optional<CreatedEvent> findAccountEvent(String partyId, String accountId) {
        return ledgerCommandService.getActiveContracts(partyId, buildEventFormat(partyId))
                .stream()
                .filter(e -> e.getTemplateId().getEntityName().equals("CollateralAccount"))
                .filter(e -> {
                    CollateralAccount.Contract c = CollateralAccount.Contract.fromCreatedEvent(e);
                    return c.data.accountId.equals(accountId);
                })
                .findFirst();
    }

    private EventFormat buildEventFormat(String partyId) {
        return new EventFormat(
                Map.of(
                        partyId,
                        new CumulativeFilter(
                                Map.of(),
                                Map.of(),
                                Optional.of(Filter.Wildcard.HIDE_CREATED_EVENT_BLOB)
                        )
                ),
                Optional.empty(),
                true
        );
    }
}