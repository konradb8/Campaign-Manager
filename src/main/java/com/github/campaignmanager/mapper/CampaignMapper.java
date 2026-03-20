package com.github.campaignmanager.mapper;

import com.github.campaignmanager.dto.response.CampaignResponse;
import com.github.campaignmanager.model.Campaign;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CampaignMapper {
    public Optional<CampaignResponse> toResponse(Campaign campaign) {
        return Optional.ofNullable(campaign)
                .map(c -> CampaignResponse.builder()
                        .id(c.getId())
                        .campaignName(c.getCampaignName())
                        .keywords(c.getKeywords())
                        .bidAmount(c.getBidAmount())
                        .campaignFund(c.getCampaignFund())
                        .status(c.getStatus())
                        .town(c.getTown())
                        .radius(c.getRadius())
                        .build()
                );

    }
}
