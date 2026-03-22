package com.github.campaignmanager.dto.response;

import com.github.campaignmanager.model.CampaignStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CampaignResponse(
        UUID id,
        String campaignName,
        List<String> keywords,
        BigDecimal bidAmount,
        BigDecimal campaignFund,
        CampaignStatus status,
        String town,
        Integer radius
) {
}
