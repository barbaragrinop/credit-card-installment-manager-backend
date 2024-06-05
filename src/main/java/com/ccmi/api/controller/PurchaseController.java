package com.ccmi.api.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ccmi.api.dto.PurchaseDTO;
import com.ccmi.api.entity.Card;
import com.ccmi.api.entity.Purchase;
import com.ccmi.api.repository.CardRepository;
import com.ccmi.api.repository.UserRepository;
import com.ccmi.api.service.CardService;
import com.ccmi.api.service.PurchaseService;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService _purchaseService;

    @Autowired
    private CardService _cardService;

    @Autowired
    private UserRepository _userRepository;

    @Autowired
    private ModelMapper _modelMapper;

    @PostMapping
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseDTO purchaseDTO) {

        Card card = _cardService.getCardById(purchaseDTO.getCardId());

        if(card == null) {
            return ResponseEntity.badRequest().build();
        }

        Purchase purchaseEntity = _modelMapper.map(purchaseDTO, Purchase.class);

        purchaseEntity.setCard(card);

        Purchase createdPurchase = _purchaseService.createPurchase(purchaseEntity);

        if(createdPurchase == null) {
            return ResponseEntity.badRequest().build();
        }

        PurchaseDTO createdPurchaseDTO = _modelMapper.map(createdPurchase, PurchaseDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdPurchaseDTO.getId()).toUri();

        return ResponseEntity.created(location).body(createdPurchaseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDTO> getPurchaseById(@PathVariable Long id) {
        Purchase purchase = _purchaseService.getPurchaseById(id);

        if(purchase == null) {
            return ResponseEntity.notFound().build();
        }

        PurchaseDTO purchaseDTO = _modelMapper.map(purchase, PurchaseDTO.class);

        return ResponseEntity.ok(purchaseDTO);  
    }

    @GetMapping("/get-purchases-by-userId")
    public ResponseEntity<?> getPurchasesByUserId(@RequestParam Long userId) {
        boolean userExists = _userRepository.existsById(userId);

        if(!userExists) {
            return ResponseEntity.badRequest().build();
        }

        List<Purchase> purchases = _purchaseService.getPurchasesByUserId(userId);

        var currentMonth = java.time.LocalDate.now().getMonthValue();

        List<Purchase> updatedPurchases = purchases
            .stream()
            .filter(purchase -> purchase.getDate().getMonthValue() >= currentMonth)
            .collect(Collectors.toList());


        return ResponseEntity.ok(updatedPurchases);
    }
}
