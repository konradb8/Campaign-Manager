package com.github.campaignmanager.controller;


import com.github.campaignmanager.service.EmeraldAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/")
public class EmeraldAccountController {

    private final EmeraldAccountService emeraldAccountService;

    @GetMapping("/account-balance")
    ResponseEntity<BigDecimal> getCampaign(
    ) {
        BigDecimal balance = emeraldAccountService.getEmeraldAccountBalance();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(balance);
    }

}
