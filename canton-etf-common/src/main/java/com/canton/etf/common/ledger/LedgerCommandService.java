package com.canton.etf.common.ledger;

import com.daml.ledger.api.v2.CommandServiceGrpc;
import com.daml.ledger.javaapi.data.*;
import com.daml.ledger.api.v2.StateServiceGrpc;
import com.daml.ledger.javaapi.data.codegen.ContractTypeCompanion;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.daml.ledger.javaapi.data.GetActiveContractsRequest;
import com.daml.ledger.javaapi.data.EventFormat;

import java.util.*;

@Service
public class LedgerCommandService {

    public static final Logger log = LoggerFactory.getLogger(LedgerCommandService.class);

    private final CommandServiceGrpc.CommandServiceBlockingStub commandService;
    private final StateServiceGrpc.StateServiceBlockingStub stateService;

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
                commands);

        commandService.submitAndWait(
                com.daml.ledger.api.v2.CommandServiceOuterClass.SubmitAndWaitRequest
                        .newBuilder()
                        .setCommands(submission.toProto())
                        .build());

        return commandId;
    }

    public List<CreatedEvent> getActiveContracts(String partyId, EventFormat eventFormat) {
        var request = new GetActiveContractsRequest(eventFormat, 0L);
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
}