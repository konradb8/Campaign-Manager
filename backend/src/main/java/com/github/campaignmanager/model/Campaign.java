package com.github.campaignmanager.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @NotNull(message = "Campaign name is mandatory")
    String campaignName;

    @ElementCollection
    @CollectionTable(
            name = "keywords",
            joinColumns = @JoinColumn(name = "campaign_id")
    )
    @NotNull(message = "Keywords are mandatory")
    List<String> keywords;

    @NotNull(message = "Bid amount is mandatory")
    @Min(value = 0, message = "Bid amount must be greater than 0")
    BigDecimal bidAmount;

    @NotNull(message = "Campaign fund is mandatory")
    BigDecimal campaignFund;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is mandatory")
    CampaignStatus status;

    String town;

    @NotNull(message = "Radius is mandatory")
    Integer radius;

}
