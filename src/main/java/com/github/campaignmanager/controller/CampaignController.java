package com.github.campaignmanager.controller;

import com.github.campaignmanager.dto.request.CampaignCreateRequest;
import com.github.campaignmanager.dto.response.CampaignResponse;
import com.github.campaignmanager.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/")
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping("/campaigns")
    ResponseEntity<CampaignResponse> createCampaign(@Valid @RequestBody CampaignCreateRequest request) {
        CampaignResponse response = campaignService.createCampaign(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


}
