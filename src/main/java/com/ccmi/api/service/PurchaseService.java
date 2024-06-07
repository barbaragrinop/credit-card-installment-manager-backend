package com.ccmi.api.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ccmi.api.dto.InstallmentsDTO;
import com.ccmi.api.entity.Purchase;
import com.ccmi.api.repository.PurchaseRepository;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository _purchaseRepository;

    public PurchaseService() { }

    public Purchase createPurchase(Purchase purchase) {
        return _purchaseRepository.save(purchase);
    }

    public Purchase getPurchaseById(Long id) {
        return _purchaseRepository.findById(id).orElse(null);
    }

    public List<Purchase> getPurchasesByUserId(Long userId) {
        return _purchaseRepository.findByUserId(userId);
    }

    public List<InstallmentsDTO> getCardInstallmentsByCardId(Long cardId) {
        return _purchaseRepository.findByCardId(cardId);
    }

}
