package com.canton.etf.api.controller;

import com.canton.etf.api.dto.CreateEtfRequest;
import com.canton.etf.api.service.EtfService;
import com.canton.etf.common.security.CantonPartyResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

@WebMvcTest(controllers = EtfController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@TestPropertySource(properties = "spring.security.enabled=false")
class EtfControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EtfService etfService;

    @MockitoBean
    private CantonPartyResolver partyResolver;

    @Test
    @WithMockUser
    void createEtf_validRequest_returns201() throws Exception {
        var request = new CreateEtfRequest(
                "SPY",
                "SPDR S&P 500",
                "78462F103",
                "Custodian::abc123",
                "Compliance::abc123",
                "Auditor::abc123"
        );

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");
        when(etfService.createEtf(eq("FundManager::abc123"), any())).thenReturn("SPY");

        mvc.perform(post("/etf")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("SPY"));
    }

    @Test
    @WithMockUser
    void getEtf_validRequest_returns200() throws Exception {
        var etfResponse = new com.canton.etf.api.dto.EtfResponse(
                "canton::etf::001",
                "SPY",
                "SPDR S&P 500",
                "78462F103",
                "Active",
                "FundManager::abc123",
                "Custodian::abc123",
                "Compliance::abc123",
                "Auditor::abc123",
                java.time.LocalDate.of(2025, 1, 15)
        );

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");
        when(etfService.getEtf(eq("FundManager::abc123"), eq("SPY"))).thenReturn(etfResponse);

        mvc.perform(get("/etf/SPY")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(etfResponse)));
    }

    @Test
    @WithMockUser
    void createEtf_missingAuthHeader_returns4xx() throws Exception {
        var request = new CreateEtfRequest(
                "SPY",
                "SPDR S&P 500",
                "78462F103",
                "Custodian::abc123",
                "Compliance::abc123",
                "Auditor::abc123"
        );

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");
        when(etfService.createEtf(eq("FundManager::abc123"), any())).thenReturn("SPY");

        mvc.perform(post("/etf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void suspendEtf_validRequest_returns204() throws Exception {
        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(put("/etf/SPY/suspend")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}