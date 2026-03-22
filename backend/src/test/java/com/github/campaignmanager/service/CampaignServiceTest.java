package com.github.campaignmanager.service;

import com.github.campaignmanager.dto.request.CampaignCreateRequest;
import com.github.campaignmanager.dto.request.CampaignUpdateRequest;
import com.github.campaignmanager.dto.response.CampaignResponse;
import com.github.campaignmanager.exception.InsufficientFundsException;
import com.github.campaignmanager.exception.WrongBidAmountException;
import com.github.campaignmanager.mapper.CampaignMapper;
import com.github.campaignmanager.model.Campaign;
import com.github.campaignmanager.model.CampaignStatus;
import com.github.campaignmanager.repository.CampaignRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private EmeraldAccountService emeraldAccountService;

    @Spy
    private CampaignMapper campaignMapper = new CampaignMapper();

    @InjectMocks
    private CampaignService underTest;

    static final UUID UUID1 = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Test
    @DisplayName("Should create campaign successfully")
    void createCampaign_success() {
        // given
        CampaignCreateRequest request = createCampaignCreateRequestBuilder()
                .build();

        when(campaignRepository.save(any(Campaign.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(emeraldAccountService.getEmeraldAccountBalance()).thenReturn(BigDecimal.valueOf(1000));

        // when
        CampaignResponse campaign = underTest.createCampaign(request);

        // then
        assertThat(campaign)
                .isNotNull()
                .extracting(
                        CampaignResponse::campaignName,
                        CampaignResponse::keywords,
                        CampaignResponse::campaignFund,
                        CampaignResponse::bidAmount,
                        CampaignResponse::status,
                        CampaignResponse::town,
                        CampaignResponse::radius
                )
                .containsExactly(
                        request.campaignName(),
                        request.keywords(),
                        request.campaignFund(),
                        request.bidAmount(),
                        request.status(),
                        request.town(),
                        request.radius()
                );

        verify(campaignRepository).save(any(Campaign.class));
        verify(emeraldAccountService).getEmeraldAccountBalance();
        verify(emeraldAccountService).deductFromBalance(request.campaignFund());
        verifyNoMoreInteractions(campaignRepository, emeraldAccountService);
    }

    @Test
    @DisplayName("Should not create campaign because of insufficient funds")
    void createCampaign_failedDueToInsufficientFunds() {

        // given
        CampaignCreateRequest request = createCampaignCreateRequestBuilder()
                .campaignFund(BigDecimal.valueOf(2500))
                .build();

        when(emeraldAccountService.getEmeraldAccountBalance()).thenReturn(BigDecimal.valueOf(1000));

        // when & then
        assertThatThrownBy(() -> underTest.createCampaign(request))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessage("Insufficient balance in Emerald account");

        verify(emeraldAccountService).getEmeraldAccountBalance();
        verifyNoMoreInteractions(campaignRepository, emeraldAccountService);
    }

    @Test
    @DisplayName("Should update campaign successfully")
    void updateCampaign_success() {
        // given
        Campaign existingCampaign = createExistingCampaign();

        CampaignUpdateRequest request = createCampaignUpdateRequestBuilder()
                .build();

        when(campaignRepository.findById(UUID1)).thenReturn(Optional.of(existingCampaign));
        when(campaignRepository.save(any(Campaign.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(emeraldAccountService.getEmeraldAccountBalance()).thenReturn(BigDecimal.valueOf(3000));

        // when
        CampaignResponse response = underTest.updateCampaign(UUID1, request);

        // then
        assertThat(response)
                .isNotNull()
                .extracting(
                        CampaignResponse::campaignName,
                        CampaignResponse::keywords,
                        CampaignResponse::campaignFund,
                        CampaignResponse::bidAmount,
                        CampaignResponse::status,
                        CampaignResponse::town,
                        CampaignResponse::radius
                )
                .containsExactly(
                        request.campaignName(),
                        request.keywords(),
                        request.campaignFund(),
                        request.bidAmount(),
                        request.status(),
                        request.town(),
                        request.radius()
                );

        verify(campaignRepository).save(any(Campaign.class));
        verify(emeraldAccountService).getEmeraldAccountBalance();
        verify(emeraldAccountService).deductFromBalance(request.campaignFund().subtract(existingCampaign.getCampaignFund()));
        verifyNoMoreInteractions(campaignRepository, emeraldAccountService);
    }

    @Test
    @DisplayName("Should not update campaign because of wrong bid amount")
    void updateCampaign_failedDueToWrongBidAmount() {
        // given
        Campaign existingCampaign = createExistingCampaign();

        CampaignUpdateRequest request = createCampaignUpdateRequestBuilder()
                .bidAmount(BigDecimal.valueOf(3000))
                .build();

        when(campaignRepository.findById(UUID1)).thenReturn(Optional.of(existingCampaign));
        when(emeraldAccountService.getEmeraldAccountBalance()).thenReturn(BigDecimal.valueOf(2000));

        // when & then
        assertThatThrownBy(() -> underTest.updateCampaign(UUID1, request))
                .isInstanceOf(WrongBidAmountException.class)
                .hasMessage("Bid amount cannot be greater than campaign fund");

        verify(campaignRepository).findById(UUID1);
        verify(emeraldAccountService).getEmeraldAccountBalance();
    }

    @Test
    @DisplayName("Should get campaign successfully")
    void getCampaign_success() {

        // given
        Campaign existingCampaign = createExistingCampaign();

        when(campaignRepository.findById(UUID1)).thenReturn(Optional.of(existingCampaign));

        // when
        CampaignResponse response = underTest.getCampaign(UUID1);

        // then
        assertThat(response)
                .isNotNull()
                .extracting(
                        CampaignResponse::campaignName,
                        CampaignResponse::keywords,
                        CampaignResponse::campaignFund,
                        CampaignResponse::bidAmount,
                        CampaignResponse::status,
                        CampaignResponse::town,
                        CampaignResponse::radius
                )
                .containsExactly(
                        existingCampaign.getCampaignName(),
                        existingCampaign.getKeywords(),
                        existingCampaign.getCampaignFund(),
                        existingCampaign.getBidAmount(),
                        existingCampaign.getStatus(),
                        existingCampaign.getTown(),
                        existingCampaign.getRadius()
                );

        verify(campaignRepository).findById(UUID1);
        verifyNoMoreInteractions(campaignRepository, emeraldAccountService);
    }

    @Test
    @DisplayName("Should not get campaign because of non-existing campaign")
    void getCampaign_failedDueToNonExistingCampaign() {
        // given
        when(campaignRepository.findById(UUID1)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> underTest.getCampaign(UUID1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Campaign not found");

        verify(campaignRepository).findById(UUID1);
        verifyNoMoreInteractions(campaignRepository, emeraldAccountService);
    }

    @Test
    @DisplayName("Should delete campaign successfully")
    void deleteCampaign_success() {
        // given
        Campaign existingCampaign = createExistingCampaign();

        when(campaignRepository.findById(UUID1)).thenReturn(Optional.of(existingCampaign));

        // when
        underTest.deleteCampaign(UUID1);

        // then
        verify(campaignRepository).findById(UUID1);
        verify(campaignRepository).delete(existingCampaign);
        verifyNoMoreInteractions(campaignRepository, emeraldAccountService);
    }

    @Test
    @DisplayName("Should not delete campaign because of non-existing campaign")
    void deleteCampaign_failedDueToNonExistingCampaign() {
        // given
        when(campaignRepository.findById(UUID1)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> underTest.deleteCampaign(UUID1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Campaign not found");

        verify(campaignRepository).findById(UUID1);
        verifyNoMoreInteractions(campaignRepository, emeraldAccountService);
    }

    private CampaignCreateRequest.CampaignCreateRequestBuilder createCampaignCreateRequestBuilder() {
        return CampaignCreateRequest.builder()
                .campaignName("Test Campaign")
                .keywords(List.of("test", "campaign"))
                .campaignFund(BigDecimal.valueOf(500))
                .bidAmount(BigDecimal.valueOf(50))
                .status(CampaignStatus.ON)
                .town("Test Town")
                .radius(10);
    }

    private CampaignUpdateRequest.CampaignUpdateRequestBuilder createCampaignUpdateRequestBuilder() {
        return CampaignUpdateRequest.builder()
                .campaignName("Updated Campaign")
                .keywords(List.of("Update", "test", "campaign"))
                .campaignFund(BigDecimal.valueOf(2500))
                .bidAmount(BigDecimal.valueOf(500))
                .status(CampaignStatus.OFF)
                .town("Updated Town")
                .radius(100);
    }

    private Campaign createExistingCampaign() {
        return Campaign.builder()
                .id(UUID1)
                .campaignName("Existing Campaign")
                .keywords(List.of("existing", "campaign"))
                .campaignFund(BigDecimal.valueOf(1000))
                .bidAmount(BigDecimal.valueOf(100))
                .status(CampaignStatus.ON)
                .town("Existing Town")
                .radius(20)
                .build();
    }
}
