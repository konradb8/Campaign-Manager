package com.github.campaignmanager.repository;

import com.github.campaignmanager.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
}
