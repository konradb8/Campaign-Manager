package com.github.campaignmanager.service;

import com.github.campaignmanager.model.EmeraldAccount;
import com.github.campaignmanager.repository.EmeraldAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class EmeraldAccountService {

    private final EmeraldAccountRepository emeraldAccountRepository;

    @Transactional
    public void deductFromBalance(BigDecimal amount) {

        emeraldAccountRepository.findAll()
                .stream()
                .findFirst()
                .map(account -> {
                    BigDecimal newBalance = account.getBalance().subtract(amount);
                    account.setBalance(newBalance);
                    return emeraldAccountRepository.save(account);
                })
                .orElseThrow(() -> new RuntimeException("Emerald account not found"));

    }

    @Transactional(readOnly = true)
    public BigDecimal getEmeraldAccountBalance() {

        return emeraldAccountRepository.findAll()
                .stream()
                .findFirst()
                .map(EmeraldAccount::getBalance)
                .orElseThrow(() -> new RuntimeException("Emerald account not found"));

    }
}
