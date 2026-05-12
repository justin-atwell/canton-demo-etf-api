package com.canton.etf.api.controller;

import com.canton.etf.api.dto.CreateRebalanceRequest;
import com.canton.etf.api.service.RebalanceService;
import com.canton.etf.common.security.CantonPartyResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RebalanceController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@TestPropertySource(properties = "spring.security.enabled=false")
class RebalanceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RebalanceService rebalanceService;

    @MockitoBean
    private CantonPartyResolver partyResolver;

    @Test
    @WithMockUser
    void createRebalance_validRequest_returns201() throws Exception {
        var weights = new ArrayList<CreateRebalanceRequest.WeightEntry>();
        weights.add(new CreateRebalanceRequest.WeightEntry("USD", 100));

        var request = new CreateRebalanceRequest(
                "SPY",
                weights
        );

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");
        when(rebalanceService.propose(any(), any(),any())).thenReturn("proposal-stub");

        mvc.perform(post("/etf/SPY/rebalance")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("proposal-stub"));
    }

    @Test
    @WithMockUser
    void getProposal_validRequest_returns200() throws Exception {
        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");
        when(rebalanceService.propose(any(), any(),any())).thenReturn("proposal-stub");

        mvc.perform(MockMvcRequestBuilders.get("/etf/SPY/rebalance/aa443")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void approveProposal_validRequest_returns200() throws Exception {
        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(MockMvcRequestBuilders.put("/etf/SPY/rebalance/1/approve")
                .header("Authorization", "Bearer test-token")
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void rejectProposal_validRequest_returns200() throws Exception {
        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(MockMvcRequestBuilders.put("/etf/SPY/rebalance/1/reject")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void executeProposal_validRequest_returns200() throws Exception {
        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(MockMvcRequestBuilders.put("/etf/SPY/rebalance/1/execute")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
