package com.github.campaignmanager.controller;

import com.github.campaignmanager.dto.request.CampaignCreateRequest;
import com.github.campaignmanager.dto.response.CampaignResponse;
import com.github.campaignmanager.model.CampaignStatus;
import com.github.campaignmanager.service.CampaignService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jmx.ParentAwareNamingStrategy;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CampaignController.class)
@TestPropertySource(properties = "spring.jmx.enabled=false")
class CampaignControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CampaignService campaignService;

    @Autowired
    ObjectMapper objectMapper;

    private static final UUID UUID1 = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Test
    @DisplayName("Should return 200 OK when getting all campaigns")
    void getAllCampaigns_Return200() throws Exception {

        CampaignResponse campaignResponse = createCampaignResponse().build();
        Page<CampaignResponse> campaignPage = new PageImpl<>(List.of(campaignResponse));

        given(campaignService.getAllCampaigns(any(Pageable.class))).willReturn(campaignPage);

        mockMvc.perform(get("/api/v1/campaigns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(UUID1.toString()))
                .andExpect(jsonPath("$.content[0].campaignName").value("Test Campaign"))
                .andExpect(jsonPath("$.content[0].keywords[0]").value("keyword1"))
                .andExpect(jsonPath("$.content[0].keywords[1]").value("keyword2"))
                .andExpect(jsonPath("$.content[0].campaignFund").value(1000.00))
                .andExpect(jsonPath("$.content[0].bidAmount").value(10.00))
                .andExpect(jsonPath("$.content[0].status").value("ON"))
                .andExpect(jsonPath("$.content[0].town").value("Test Town"))
                .andExpect(jsonPath("$.content[0].radius").value(10));

        verify(campaignService).getAllCampaigns(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return 201 CREATED when creating a campaign")
    void createCampaign_returnsCampaignResponse() throws Exception {
        CampaignCreateRequest request = createCampaignCreateRequest().build();

        CampaignResponse response = createCampaignResponse().build();

        given(campaignService.createCampaign(request)).willReturn(response);

        mockMvc.perform(post("/api/v1/campaigns")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.campaignName").value("Test Campaign"))
                .andExpect(jsonPath("$.keywords[0]").value("keyword1"))
                .andExpect(jsonPath("$.keywords[1]").value("keyword2"))
                .andExpect(jsonPath("$.campaignFund").value(1000.00))
                .andExpect(jsonPath("$.bidAmount").value(10.00))
                .andExpect(jsonPath("$.status").value("ON"))
                .andExpect(jsonPath("$.town").value("Test Town"))
                .andExpect(jsonPath("$.radius").value(10));

        verify(campaignService).createCampaign(any());
    }

    private CampaignCreateRequest.CampaignCreateRequestBuilder createCampaignCreateRequest() {
        return CampaignCreateRequest.builder()
                .campaignName("Test Campaign")
                .keywords(List.of("keyword1", "keyword2"))
                .campaignFund(BigDecimal.valueOf(1000.00))
                .bidAmount(BigDecimal.valueOf(10.00))
                .status(CampaignStatus.ON)
                .town("Test Town")
                .radius(10);
    }

    private CampaignResponse.CampaignResponseBuilder createCampaignResponse() {
        return CampaignResponse.builder()
                .id(UUID1)
                .campaignName("Test Campaign")
                .keywords(List.of("keyword1", "keyword2"))
                .campaignFund(BigDecimal.valueOf(1000.00))
                .bidAmount(BigDecimal.valueOf(10.00))
                .status(CampaignStatus.ON)
                .town("Test Town")
                .radius(10);
    }
}
