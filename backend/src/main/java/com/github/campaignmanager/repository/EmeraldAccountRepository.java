package com.github.campaignmanager.repository;

import com.github.campaignmanager.model.EmeraldAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmeraldAccountRepository extends JpaRepository<EmeraldAccount, UUID> {
}
