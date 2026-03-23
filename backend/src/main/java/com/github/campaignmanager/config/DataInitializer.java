package com.github.campaignmanager.config;

import com.github.campaignmanager.model.EmeraldAccount;
import com.github.campaignmanager.repository.EmeraldAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final EmeraldAccountRepository emeraldAccountRepository;

    @Override
    public void run(String... args) {
        if (emeraldAccountRepository.count() == 0) {
            EmeraldAccount account = emeraldAccountRepository.save(EmeraldAccount.builder()
                    .balance(new BigDecimal("10000.00"))
                    .build());

            emeraldAccountRepository.save(account);
        }
    }

}
