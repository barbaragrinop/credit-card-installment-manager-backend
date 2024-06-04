package com.ccmi.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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




    
}
