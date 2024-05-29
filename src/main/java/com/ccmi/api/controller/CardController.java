package com.ccmi.api.controller;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ccmi.api.dto.CardDTO;
import com.ccmi.api.entity.Card;
import com.ccmi.api.entity.User;
import com.ccmi.api.service.CardService;
import com.ccmi.api.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/card")
public class CardController {

    @Autowired
    private CardService _cardService;

    @Autowired
    private UserService _userService;

    @Autowired
    private ModelMapper _mapper;

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody CardDTO card) {
        Card entity = _mapper.map(card, Card.class);

        User user = _userService.getUserDataByEmail(card.getUserEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário inválido!");
        }

        entity.setUser(user);

        Card createdCard = _cardService.createCard(entity);

        CardDTO creadtedCardDTO = _mapper.map(createdCard, CardDTO.class);

        if (createdCard == null) {
            return ResponseEntity.badRequest().body("Cartão não pode ser criado!");
        }

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdCard.getId()).toUri();

        return ResponseEntity.created(
                location).body(creadtedCardDTO);
    }

    @GetMapping("/get-cards-by-userId")
    public ResponseEntity<List<Card>> getCardsByUserId(@RequestParam Long userId) {
        return ResponseEntity.ok(_cardService.getCardsByUserId(userId));
    }

    @PutMapping
    public ResponseEntity<?> updateCard(@RequestBody CardDTO card) {
        Card entity = _mapper.map(card, Card.class);

        User user = _userService.getUserDataByEmail(card.getUserEmail());

        if (user == null) {
            return ResponseEntity.badRequest().body("Usuário inválido!");
        }

        entity.setUser(user);

        Card updatedCard = _cardService.updateCard(entity);

        CardDTO updatedCardDTO = _mapper.map(updatedCard, CardDTO.class);

        if (updatedCard == null) {
            return ResponseEntity.badRequest().body("Cartão não pode ser atualizado!");
        }

        return ResponseEntity.ok(updatedCardDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCard(@RequestParam Long id) {
      
        boolean isCardDeleted = _cardService.deleteCard(id);

        if(isCardDeleted == false){
            return ResponseEntity.badRequest().body("Cartão não pode ser deletado!");
        }

        return ResponseEntity.ok("Cartão deletado com sucesso!");
    }
}
  