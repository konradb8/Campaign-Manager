package com.github.campaignmanager.dto.request;

import com.github.campaignmanager.model.CampaignStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.List;

public record CampaignUpdateRequest(
        String campaignName,
        List<String> keywords,

        @DecimalMin(value = "0.01", message = "Minimum bid must be greater than 0")
        BigDecimal bidAmount,

        @DecimalMin(value = "0.0", message = "Campaign fund cannot be negative")
        BigDecimal campaignFund,
        CampaignStatus status,
        String town,
        @Min(value = 0, message = "Radius cannot be negative")
        Integer radius
) {

}
