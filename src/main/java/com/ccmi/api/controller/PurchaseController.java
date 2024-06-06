package com.ccmi.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import com.ccmi.api.dto.CardDTO;
import com.ccmi.api.dto.InstallmentsDTO;
import com.ccmi.api.dto.PurchaseDTO;
import com.ccmi.api.entity.Card;
import com.ccmi.api.entity.Purchase;
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

        if (card == null) {
            return ResponseEntity.badRequest().build();
        }

        Purchase purchaseEntity = _modelMapper.map(purchaseDTO, Purchase.class);

        purchaseEntity.setCard(card);

        Purchase createdPurchase = _purchaseService.createPurchase(purchaseEntity);

        if (createdPurchase == null) {
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

        if (purchase == null) {
            return ResponseEntity.notFound().build();
        }

        PurchaseDTO purchaseDTO = _modelMapper.map(purchase, PurchaseDTO.class);

        return ResponseEntity.ok(purchaseDTO);
    }

    @GetMapping("/get-purchases-by-userId")
    public ResponseEntity<?> getPurchasesByUserId(@RequestParam Long userId) {
        boolean userExists = _userRepository.existsById(userId);

        if (!userExists) {
            return ResponseEntity.badRequest().build();
        }

        List<Purchase> purchases = _purchaseService.getPurchasesByUserId(userId);

        List<InstallmentsDTO> months = purchases.stream().map(purchase -> {

            InstallmentsDTO installmentsDTO = new InstallmentsDTO();
            installmentsDTO.setCardId(purchase.getCard().getId());
            installmentsDTO.setInstallments(purchase.getInstallments());
            installmentsDTO.setProductName(purchase.getProductName());
            installmentsDTO.setStore(purchase.getStore());
            installmentsDTO.setValue(purchase.getValue());
            installmentsDTO.setDate(purchase.getDate());
            installmentsDTO.setId(purchase.getId());

            LocalDate now = LocalDate.now();
            LocalDate purchaseDate = purchase.getDate();
            Integer totalInstallments = purchase.getInstallments();
            LocalDate lastInstallmentDate = purchaseDate.plusMonths(totalInstallments - 1);

            Card card = _cardService.getCardById(purchase.getCard().getId());
            CardDTO cardDTO = _modelMapper.map(card, CardDTO.class);
            installmentsDTO.setCard(cardDTO);

            int dueDay = card.getDueDay();

            // Calculate the installments paid based on the card's due day
            int installmentsPaid = 0;
            LocalDate currentDueDate = purchaseDate.withDayOfMonth(dueDay);

            if (purchaseDate.getDayOfMonth() > dueDay) {
                currentDueDate = currentDueDate.plusMonths(1);
            }

            while (!currentDueDate.isAfter(now) && installmentsPaid < totalInstallments) {
                installmentsPaid++;
                currentDueDate = currentDueDate.plusMonths(1);
            }

            int installmentsLeft = totalInstallments - installmentsPaid;

            installmentsDTO.setInstallmentsPaid(installmentsPaid);
            installmentsDTO.setInstallmentsLeft(installmentsLeft);
            installmentsDTO.setLastInstallmentDate(lastInstallmentDate);

            // Calculate the months and whether they have been paid
            Map<String, Boolean> monthStatus = new LinkedHashMap<>();
            LocalDate tempDate = purchaseDate;
            for (int i = 0; i < totalInstallments; i++) {
                String month = tempDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toLowerCase();
                monthStatus.put(month, !tempDate.isAfter(now));
                tempDate = tempDate.plusMonths(1);
            }

            installmentsDTO.setMonths(monthStatus);

            return installmentsDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(months);
    }
}
