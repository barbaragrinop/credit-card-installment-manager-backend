package com.ccmi.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

import java.text.NumberFormat;

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

        Locale brazilLocale = new Locale("pt", "BR");
        NumberFormat numberFormat = NumberFormat.getNumberInstance(brazilLocale);
        numberFormat.setMinimumFractionDigits(2);
        numberFormat.setMaximumFractionDigits(2);

        if (!userExists) {
            return ResponseEntity.badRequest().build();
        }

        List<Purchase> purchases = _purchaseService.getPurchasesByUserId(userId);
        int currentYear = LocalDate.now().getYear();

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

            Map<String, Map<String, Object>> monthStatus = new LinkedHashMap<>();
            LocalDate tempDate = purchaseDate;
            if (purchaseDate.getDayOfMonth() > dueDay) {
                tempDate = tempDate.plusMonths(1);
            }
            tempDate = tempDate.withDayOfMonth(dueDay);

            for (int i = 0; i < totalInstallments; i++) {
                int tempYear = tempDate.getYear();
                if (tempYear == currentYear) {
                    String month = tempDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toLowerCase();
                    Map<String, Object> details = new LinkedHashMap<>();
                    boolean isPaid = !tempDate.isAfter(now);
                    details.put("isPaid", isPaid);
                    details.put("payDay", isPaid ? tempDate : null);
                    details.put("installmentNumber", i + 1);
                    monthStatus.put(month, details);
                }
                tempDate = tempDate.plusMonths(1);
            }

           // Format and parse value per installment
           String valuePerInstallment = numberFormat.format(purchase.getValue() / totalInstallments);
           installmentsDTO.setValuePerInstallment(valuePerInstallment);
   
           // Format and set value left
           String valueLeft = numberFormat.format((purchase.getValue() / totalInstallments) * installmentsLeft);
           installmentsDTO.setValueLeft(valueLeft);
   
           // Format and set value paid
           String valuePaid = numberFormat.format((purchase.getValue() / totalInstallments) * installmentsPaid);
           installmentsDTO.setValuePaid(valuePaid);

            installmentsDTO.setMonths(monthStatus);

            return installmentsDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(months);
    }

    @GetMapping("/get-purchases-by-cardId/{cardId}")
    public ResponseEntity<List<InstallmentsDTO>> getPurchaseByCardId(@PathVariable Long cardId) {
        Card cardExists = _cardService.getCardById(cardId);

        if(cardExists == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<InstallmentsDTO> purchases = _purchaseService.getCardInstallmentsByCardId(cardId);

        if (purchases == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(purchases);
    }
}
