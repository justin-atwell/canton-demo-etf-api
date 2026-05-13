package com.canton.etf.api.controller;

import com.canton.etf.api.dto.PostNavRequest;
import com.canton.etf.api.service.NAVService;
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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NAVController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        })
@TestPropertySource(properties = "spring.security.enabled=false")
public class NAVControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NAVService navService;

    @MockitoBean
    private CantonPartyResolver partyResolver;

    @Test
    @WithMockUser
    void NAV_validRequest_returns201() throws Exception {
        var request = new PostNavRequest(
                "SPY",
                12.43D,
                4494.433D,
                "ThegoodKind",
                LocalDate.now().toString()
        );

        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");
        when(navService.createNAV(any(), any(), any())).thenReturn("12");

        mvc.perform(post("/etf/spy/nav")
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("12"));
    }

    @Test
    @WithMockUser
    void NAV_noHeader_returns4xx() throws Exception {
        var request = new PostNavRequest(
                "SPY",
                12.43D,
                4494.433D,
                "ThegoodKind",
                LocalDate.now().toString()
        );

        mvc.perform(post("/etf/spy/nav")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void getNAV_validRequest_returns200() throws Exception {
        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(MockMvcRequestBuilders.get("/etf/spy/nav")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getNAV_noHeader_returns4xx() throws Exception {
        when(partyResolver.resolveParty(any())).thenReturn("FundManager::abc123");

        mvc.perform(MockMvcRequestBuilders.get("/etf/spy/nav")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
