package com.serdyuchenko.microflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.serdyuchenko.microflow.dto.CreateRequestResponse;
import com.serdyuchenko.microflow.service.RequestService;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void shouldCreateRequest() throws Exception {
        UUID requestId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        when(requestService.create(any()))
                .thenReturn(new CreateRequestResponse(requestId, "created"));

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\":\"demo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.requestId").value(requestId.toString()))
                .andExpect(jsonPath("$.status").value("created"));
    }

    @Test
    void shouldRejectBlankText() throws Exception {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"text\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
