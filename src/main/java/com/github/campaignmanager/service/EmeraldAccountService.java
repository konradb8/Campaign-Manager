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
        EmeraldAccount account = emeraldAccountRepository.findAll().getFirst();

        BigDecimal newBalance = account.getBalance().subtract(amount);

        account.setBalance(newBalance);

        emeraldAccountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public BigDecimal getEmeraldAccountBalance() {

        return emeraldAccountRepository.findAll().getLast().getBalance();
    }
}
