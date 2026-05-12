package com.canton.etf.api.controller;

import com.canton.etf.api.service.AuditService;
import com.canton.etf.api.service.EtfService;
import com.canton.etf.common.security.CantonPartyResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuditController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@TestPropertySource(properties = "spring.security.enabled=false")
public class AuditControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuditService auditService;

    @MockitoBean
    private CantonPartyResolver partyResolver;

    @Test
    @WithMockUser
    void getAccessEvents_valid_returns200() throws Exception {
        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");
        when(auditService.getAccessEvents("FundManager::abc123")).thenReturn("defnotnull");

        mvc.perform(get("/audit")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("defnotnull"));
    }
}
