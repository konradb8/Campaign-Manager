package com.github.campaignmanager.service;

import com.github.campaignmanager.dto.request.CampaignCreateRequest;
import com.github.campaignmanager.dto.request.CampaignUpdateRequest;
import com.github.campaignmanager.dto.response.CampaignResponse;
import com.github.campaignmanager.exception.InsufficientFundsException;
import com.github.campaignmanager.exception.WrongBidAmountException;
import com.github.campaignmanager.mapper.CampaignMapper;
import com.github.campaignmanager.model.Campaign;
import com.github.campaignmanager.repository.CampaignRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper mapper;
    private final EmeraldAccountService emeraldAccountService;


    @Transactional
    public CampaignResponse createCampaign(CampaignCreateRequest request) {

        checkEmeraldAccountBalance(request.campaignFund());
        checkBidAmount(request.bidAmount(), request.campaignFund());

        Campaign campaign = Campaign.builder()
                .campaignName(request.campaignName())
                .keywords(request.keywords())
                .campaignFund(request.campaignFund())
                .bidAmount(request.bidAmount())
                .status(request.status())
                .town(request.town())
                .radius(request.radius())
                .build();

        emeraldAccountService.deductFromBalance(campaign.getCampaignFund());

        return mapToResponse(campaignRepository.save(campaign));
    }

    @Transactional
    public CampaignResponse updateCampaign(UUID campaignId, CampaignUpdateRequest request) {
        Campaign campaign = getCampaignById(campaignId);

        if (request.campaignFund() != null) {
            BigDecimal newCampaignFund = request.campaignFund();
            BigDecimal existingCampaignFund = campaign.getCampaignFund();

            if (newCampaignFund.compareTo(existingCampaignFund) > 0) {
                BigDecimal diff = newCampaignFund.subtract(existingCampaignFund);
                checkEmeraldAccountBalance(diff);
                emeraldAccountService.deductFromBalance(diff);
            }
        }

        Campaign updated = Campaign.builder()
                .id(campaign.getId())
                .campaignName(request.campaignName() != null ? request.campaignName() : campaign.getCampaignName())
                .keywords(request.keywords() != null ? request.keywords() : campaign.getKeywords())
                .bidAmount(request.bidAmount() != null ? request.bidAmount() : campaign.getBidAmount())
                .campaignFund(request.campaignFund() != null ? request.campaignFund() : campaign.getCampaignFund())
                .status(request.status() != null ? request.status() : campaign.getStatus())
                .town(request.town() != null ? request.town() : campaign.getTown())
                .radius(request.radius() != null ? request.radius() : campaign.getRadius())
                .build();

        checkBidAmount(updated.getBidAmount(), updated.getCampaignFund());

        return mapToResponse(campaignRepository.save(updated));
    }

    @Transactional
    public void deleteCampaign(UUID campaignId) {
        Campaign campaign = getCampaignById(campaignId);
        campaignRepository.delete(campaign);
    }

    @Transactional(readOnly = true)
    public CampaignResponse getCampaign(UUID campaignId) {
        Campaign campaign = getCampaignById(campaignId);
        return mapToResponse(campaign);
    }

    @Transactional(readOnly = true)
    public Page<CampaignResponse> getAllCampaigns(Pageable pageable) {
        Page<Campaign> campaigns = campaignRepository.findAll(pageable);
        return campaigns.map(this::mapToResponse);
    }

    private CampaignResponse mapToResponse(Campaign campaign) {
        return mapper.toResponse(campaign)
                .orElseThrow(() -> new IllegalStateException("Failed to map response"));
    }

    private Campaign getCampaignById(UUID campaignId) {
        return campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));
    }

    private void checkEmeraldAccountBalance(BigDecimal amount) {
        if (amount.compareTo(emeraldAccountService.getEmeraldAccountBalance()) > 0) {
            throw new InsufficientFundsException("Insufficient balance in Emerald account");
        }
    }

    private void checkBidAmount(BigDecimal bidAmount, BigDecimal campaignFund) {
        if (bidAmount.compareTo(campaignFund) > 0) {
            throw new WrongBidAmountException("Bid amount cannot be greater than campaign fund");
        }
    }
}
