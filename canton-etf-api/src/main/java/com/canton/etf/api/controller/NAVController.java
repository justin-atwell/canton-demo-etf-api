package com.canton.etf.api.controller;

import com.canton.etf.api.dto.PostNavRequest;
import com.canton.etf.api.service.NAVService;
import com.canton.etf.common.security.CantonPartyResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/etf/{ticker}/nav")
public class NAVController {

    private final NAVService navService;
    private final CantonPartyResolver partyResolver;

    public NAVController(NAVService navService, CantonPartyResolver partyResolver) {
        this.navService = navService;
        this.partyResolver = partyResolver;
    }

    @PostMapping
    public ResponseEntity<String> nav(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody PostNavRequest request,
            @PathVariable String ticker){

        String partyId = partyResolver.resolveParty(authHeader);
        String navId = navService.createNAV(partyId, ticker, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(navId);
    }

    @GetMapping
    public ResponseEntity<?> getNAV(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String ticker){
        String partyId = partyResolver.resolveParty(authHeader);

        return ResponseEntity.ok(navService.getNAV(partyId, ticker));
    }
}
