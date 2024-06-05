package com.ccmi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ccmi.api.entity.Card;
import java.util.List;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByUserId(Long userId);

    @Query("SELECT c FROM Card c WHERE c.name = ?1")
    Card findByName(String name);

}
