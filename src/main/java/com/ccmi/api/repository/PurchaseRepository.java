package com.ccmi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccmi.api.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long>{ }
