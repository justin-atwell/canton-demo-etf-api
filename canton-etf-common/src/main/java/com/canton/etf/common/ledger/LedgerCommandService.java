package com.canton.etf.common.ledger;

import com.daml.ledger.api.v2.CommandServiceGrpc;
import com.daml.ledger.javaapi.data.*;
import com.daml.ledger.api.v2.StateServiceGrpc;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.daml.ledger.javaapi.data.GetActiveContractsRequest;
import com.daml.ledger.javaapi.data.EventFormat;

import java.util.*;

@Service
public class LedgerCommandService {

    public static final Logger log = LoggerFactory.getLogger(LedgerCommandService.class);

    private final CommandServiceGrpc.CommandServiceBlockingStub commandService;
    private final StateServiceGrpc.StateServiceBlockingStub stateService;

    @Autowired
    public LedgerCommandService(ManagedChannel channel) {
        this.commandService = CommandServiceGrpc.newBlockingStub(channel);
        this.stateService = StateServiceGrpc.newBlockingStub(channel);
    }

    LedgerCommandService(
            CommandServiceGrpc.CommandServiceBlockingStub commandService,
            StateServiceGrpc.StateServiceBlockingStub stateService) {
        this.commandService = commandService;
        this.stateService = stateService;
    }

    public String submitAndWait(
            String partyId,
            String applicationId,
            List<Command> commands) {

        String commandId = UUID.randomUUID().toString();

        log.info("Submitting {} commands as party {} commandId {}",
                commands.size(), partyId, commandId);

        var submission = CommandsSubmission.create(
                applicationId,
                commandId,
                java.util.Optional.empty(),
                commands).withActAs(partyId);

        commandService.submitAndWait(
                com.daml.ledger.api.v2.CommandServiceOuterClass.SubmitAndWaitRequest
                        .newBuilder()
                        .setCommands(submission.toProto())
                        .build());

        log.info("submitAndWait completed successfully for commandId={}", commandId);

        return commandId;
    }

    // -------------------------------------------------------------------------
    // submitAndWaitWithSubmission
    //   Used when the caller needs full control over the CommandsSubmission —
    //   e.g. multi-party actAs (Suspend requires fundManager + compliance).
    // -------------------------------------------------------------------------
    public String submitAndWaitWithSubmission(CommandsSubmission submission) {
        log.info("Submitting multi-party command commandId={} actAs={}",
                submission.getCommandId(), submission.getActAs());

        commandService.submitAndWait(
                com.daml.ledger.api.v2.CommandServiceOuterClass.SubmitAndWaitRequest
                        .newBuilder()
                        .setCommands(submission.toProto())
                        .build());

        log.info("submitAndWaitWithSubmission completed successfully for commandId={}",
                submission.getCommandId());

        return submission.getCommandId();
    }

    public List<CreatedEvent> getActiveContracts(String partyId, EventFormat eventFormat) {
        var request = new GetActiveContractsRequest(eventFormat, getLedgerEnd());
        var results = new ArrayList<CreatedEvent>();
        stateService.getActiveContracts(request.toProto()).forEachRemaining(response -> {
            if (response.hasActiveContract()) {
                results.add(CreatedEvent.fromProto(
                        response.getActiveContract().getCreatedEvent()
                ));
            }
        });
        log.info("getActiveContracts partyId={} found={}", partyId, results.size());

        return results;
    }

    private long getLedgerEnd() {
        var request = com.daml.ledger.api.v2.StateServiceOuterClass
                .GetLedgerEndRequest.newBuilder().build();

        var response = stateService.getLedgerEnd(request);
        return response.getOffset();
    }
}