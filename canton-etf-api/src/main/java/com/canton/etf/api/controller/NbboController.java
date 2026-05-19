package com.canton.etf.api.controller;

import com.canton.etf.api.dto.NBBOQuoteResponse;
import com.canton.etf.api.service.NbboService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nbbo")
public class NbboController {

    private final NbboService nbboService;
    private final CantonPartyResolver partyResolver;

    public NbboController(NbboService nbboService,
                          CantonPartyResolver partyResolver) {
        this.nbboService = nbboService;
        this.partyResolver = partyResolver;
    }

    @GetMapping
    public ResponseEntity<List<NBBOQuoteResponse>> getLatestQuotes(
            @RequestHeader("Authorization") String authHeader) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(nbboService.getLatestQuotes(partyId));
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<List<NBBOQuoteResponse>> getQuotesByTicker(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker) {
        String partyId = partyResolver.resolveParty(authHeader);
        return ResponseEntity.ok(nbboService.getQuotesByTicker(partyId, ticker));
    }
}