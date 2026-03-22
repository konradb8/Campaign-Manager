package com.github.campaignmanager.service;

import com.github.campaignmanager.model.EmeraldAccount;
import com.github.campaignmanager.repository.EmeraldAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmeraldAccountServiceTest {

    @Mock
    private EmeraldAccountRepository emeraldAccountRepository;

    @InjectMocks
    private EmeraldAccountService underTest;

    @Test
    @DisplayName("Should deduct amount from balance and save updated account")
    void deductFromBalance_DeductAmountFromBalance() {
        // given

        EmeraldAccount account = EmeraldAccount.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();

        when(emeraldAccountRepository.findAll()).thenReturn(List.of(account));
        when(emeraldAccountRepository.save(account)).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        underTest.deductFromBalance(BigDecimal.valueOf(200));

        // then
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(800));

        verify(emeraldAccountRepository).findAll();
        verify(emeraldAccountRepository).save(account);
    }

    @Test
    @DisplayName("Should return current balance from account")
    void getEmeraldAccountBalance_ReturnCurrentBalance() {
        // given
        EmeraldAccount account = EmeraldAccount.builder()
                .balance(BigDecimal.valueOf(1500))
                .build();

        when(emeraldAccountRepository.findAll()).thenReturn(List.of(account));

        // when
        BigDecimal balance = underTest.getEmeraldAccountBalance();

        // then
        assertThat(balance).isEqualByComparingTo(BigDecimal.valueOf(1500));

        verify(emeraldAccountRepository).findAll();
    }

    @Test
    @DisplayName("Should handle negative balance when deducting amount greater than current balance")
    void deductFromBalance_ShouldHandleNegativeBalance() {
        // given
        EmeraldAccount account = EmeraldAccount.builder()
                .balance(BigDecimal.valueOf(100))
                .build();

        when(emeraldAccountRepository.findAll()).thenReturn(List.of(account));
        when(emeraldAccountRepository.save(account)).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        underTest.deductFromBalance(BigDecimal.valueOf(150));

        // then
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(-50));

        verify(emeraldAccountRepository).findAll();
        verify(emeraldAccountRepository).save(account);
    }

    @Test
    @DisplayName("Should handle empty account list when getting balance")
    void getEmeraldAccountBalance_ShouldHandleEmptyAccountList() {
        // given
        when(emeraldAccountRepository.findAll()).thenReturn(List.of());

        // when & then
        assertThatThrownBy(() -> underTest.getEmeraldAccountBalance())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Emerald account not found");

        verify(emeraldAccountRepository).findAll();
    }
}
