package com.ccmi.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccmi.api.entity.Card;
import com.ccmi.api.repository.CardRepository;

import java.util.*;

@Service
public class CardService {

    @Autowired
    private CardRepository _cardRepository;

    public CardService() { }

    public Card createCard(Card card) {
        return _cardRepository.save(card);
    }

    public List<Card> getCardsByUserId(Long userId) {
        // get cards by user email
        return _cardRepository.findAllByUserId(userId);
    }

    public Card getCardById(Long id) {
        Optional<Card> card = _cardRepository.findById(id);
        return card.orElse(null);
    }

    public Card updateCard(Card card) {
        Card cardExists = getCardById(card.getId());

        if (cardExists == null) {
            return null;
        }

        cardExists.setName(card.getName());
        cardExists.setDueDay(card.getDueDay());
        cardExists.setCardBrand(card.getCardBrand());

        return _cardRepository.save(cardExists);
    }

    public boolean deleteCard(Long id) {
        Card card = getCardById(id);

        if (card == null) {
            return false;
        }

        _cardRepository.delete(card);

        return true;
    }

    public boolean verifyCardExists(String name) {
        Card exists = _cardRepository.findByName(name);

        if (exists == null) {
            return false;
        }

        return true;
    }

}
