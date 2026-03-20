package com.github.campaignmanager.service;

import com.github.campaignmanager.dto.request.CampaignCreateRequest;
import com.github.campaignmanager.dto.response.CampaignResponse;
import com.github.campaignmanager.mapper.CampaignMapper;
import com.github.campaignmanager.model.Campaign;
import com.github.campaignmanager.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper mapper;


    @Transactional
    public CampaignResponse createCampaign(CampaignCreateRequest request) {

        Campaign campaign = Campaign.builder()
                .campaignName(request.campaignName())
                .keywords(request.keywords())
                .campaignFund(request.campaignFund())
                .bidAmount(request.bidAmount())
                .status(request.status())
                .town(request.town())
                .radius(request.radius())
                .build();

        Campaign saved = campaignRepository.save(campaign);

        return mapper.toResponse(saved)
                .orElseThrow(() -> new IllegalStateException("Failed to map response"));
    }
}
