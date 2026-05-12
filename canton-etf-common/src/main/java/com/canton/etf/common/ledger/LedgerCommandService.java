package com.canton.etf.common.ledger;

import com.daml.ledger.api.v2.CommandServiceGrpc;
import com.daml.ledger.api.v2.CommandServiceOuterClass.SubmitAndWaitRequest;
import com.daml.ledger.javaapi.data.Command;
import com.daml.ledger.javaapi.data.CommandsSubmission;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LedgerCommandService {

    public static final Logger log = LoggerFactory.getLogger(LedgerCommandService.class);

    private final CommandServiceGrpc.CommandServiceBlockingStub commandService;

    public LedgerCommandService(ManagedChannel channel) {
        this.commandService = CommandServiceGrpc.newBlockingStub(channel);
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

}