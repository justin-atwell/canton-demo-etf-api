package com.canton.etf.api.controller;

import com.canton.etf.api.dto.CreateCollateralAccountRequest;
import com.canton.etf.api.service.CollateralService;
import com.canton.etf.common.security.CantonPartyResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = EtfController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@TestPropertySource(properties = "spring.security.enabled=false")
public class CollateralControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CollateralService collateralService;

    @MockitoBean
    private CantonPartyResolver partyResolver;

    @Test
    @WithMockUser
    void createAccountReturns201() {
        var request = new CreateCollateralAccountRequest(
                "Custodian::abc123",
                "FundManager::abc123",
                "Compliance::abc123",
                "Auditor:abc123",
                "spy",
                0.0D,
                "a243323c"
        );

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");
        when(collateralService.createAccount("FundManager::abc123",request)).thenReturn("a243323c");


    }
}
