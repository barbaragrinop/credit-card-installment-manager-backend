package com.ccmi.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import com.ccmi.api.dto.InstallmentsDTO;
import com.ccmi.api.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p FROM Purchase p WHERE p.card.user.id = ?1")
    List<Purchase> findByUserId(Long userId);

    @Query(value = "SELECT * FROM purchase WHERE card_id = :cardId", nativeQuery = true)
    List<InstallmentsDTO> findByCardId(@Param("cardId") Long cardId);
}
