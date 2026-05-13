package com.canton.etf.api.controller;

import com.canton.etf.api.dto.CollateralTransactionRequest;
import com.canton.etf.api.dto.CreateCollateralAccountRequest;
import com.canton.etf.api.dto.LockCollateralRequest;
import com.canton.etf.api.dto.MarginCallRequest;
import com.canton.etf.api.service.CollateralService;
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
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CollateralController.class,
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
    void createAccountReturns201() throws Exception {
        String accountId = "a243323c";
        var request = new CreateCollateralAccountRequest(
                "Custodian::abc123",
                "FundManager::abc123",
                "Compliance::abc123",
                "Auditor:abc123",
                "spy",
                0.0D,
                accountId
        );

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");
        when(collateralService.createAccount("FundManager::abc123",request)).thenReturn(accountId);

        mvc.perform(post("/collateral")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(accountId));
    }

    @Test
    @WithMockUser
    void createAccount_missingAuthHeader_returns4xx() throws Exception {
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
        when(collateralService.createAccount(eq("FundManager::abc123"), any())).thenReturn("a243323c");

        mvc.perform(post("/collateral")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void depositReturns200() throws Exception {
        var request = new CollateralTransactionRequest(100.0d);

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(post("/collateral/a243323c/deposit")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    void deposit_missingAuthHeader_returns4xx() throws Exception {
        var request = new CollateralTransactionRequest(100.0d);

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(post("/collateral/a243323c/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void lockReturns200() throws Exception {
        var request = new LockCollateralRequest(50.10d, "he doesnt pay his bills", LocalDateTime.now().toString());

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(post("/collateral/a243323c/lock")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    void lock_missingAuthHeader_returns4xx() throws Exception {
        var request = new LockCollateralRequest(50.10d, "he doesnt pay his bills", LocalDateTime.now().toString());

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(post("/collateral/a243323c/lock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void withdrawReturns200() throws Exception {
        var request = new CollateralTransactionRequest(100.0d);

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(post("/collateral/a243323c/withdraw")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    void withdraw_missingAuthHeader_returns4xx() throws Exception {
        var request = new CollateralTransactionRequest(100.0d);

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(post("/collateral/a243323c/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void marginCall_Returns200() throws Exception {
        var request = new MarginCallRequest("SPY", 11.2d, LocalDateTime.now().toString());

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(post("/collateral/a243323c/margincall")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    void marginCall_missingAuthHeader_returns4xx() throws Exception {
        var request = new MarginCallRequest("SPY", 11.2d, LocalDateTime.now().toString());

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(post("/collateral/a243323c/margincall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void meet_Returns200() throws Exception {
        var request = new MarginCallRequest("SPY", 11.2d, LocalDateTime.now().toString());

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(put("/collateral/a243323c/margincall/23333/meet")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    void meet_missingAuthHeader_returns4xx() throws Exception {
        var request = new MarginCallRequest("SPY", 11.2d, LocalDateTime.now().toString());

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(put("/collateral/a243323c/margincall/223/meet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void default_Returns200() throws Exception {
        var request = new MarginCallRequest("SPY", 11.2d, LocalDateTime.now().toString());

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(put("/collateral/a243323c/margincall/23333/default")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser
    void default_missingAuthHeader_returns4xx() throws Exception {
        var request = new MarginCallRequest("SPY", 11.2d, LocalDateTime.now().toString());

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(put("/collateral/a243323c/margincall/223/default")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }
}
