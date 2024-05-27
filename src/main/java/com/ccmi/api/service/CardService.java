package com.ccmi.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccmi.api.entity.Card;
import com.ccmi.api.repository.CardRepository;

import java.util.List;

@Service
public class CardService {
    
    @Autowired
    private CardRepository _cardRepository;

    public CardService() { }

    public Card createCard(Card card) {
        return _cardRepository.save(card);
    }

    public List<Card> getCardsByUserId(Long userId) {
        //get cards by user email
        return _cardRepository.findAllByUserId(userId);
    }

}
