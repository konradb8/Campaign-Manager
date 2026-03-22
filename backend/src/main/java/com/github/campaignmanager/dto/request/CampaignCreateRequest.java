package com.github.campaignmanager.dto.request;


import com.github.campaignmanager.model.CampaignStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record CampaignCreateRequest(
        @NotBlank(message = "Campaign name is mandatory")
        @Size(min = 1)
        String campaignName,

        @NotEmpty(message = "At least one keyword is mandatory")
        List<String> keywords,

        @NotNull(message = "Bid amount is mandatory")
        @DecimalMin(value = "0.01", message = "Minimum bid must be greater than 0")
        BigDecimal bidAmount,

        @NotNull(message = "Campaign fund is mandatory")
        @DecimalMin(value = "0.0", message = "Campaign fund cannot be negative")
        BigDecimal campaignFund,

        @NotNull(message = "Status is mandatory")
        CampaignStatus status,

        String town,

        @NotNull(message = "Radius is mandatory")
        @Min(value = 0, message = "Radius cannot be negative")
        Integer radius
) {
}
