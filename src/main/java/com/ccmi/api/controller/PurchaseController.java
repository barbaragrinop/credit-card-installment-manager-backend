package com.ccmi.api.controller;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ccmi.api.dto.PurchaseDTO;
import com.ccmi.api.entity.Purchase;
import com.ccmi.api.service.PurchaseService;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService _purchaseService;

    @Autowired
    private ModelMapper _modelMapper;

    @PostMapping
    public ResponseEntity<PurchaseDTO> createPurchase(@RequestBody PurchaseDTO purchaseDTO) {

        Purchase purchaseEntity = _modelMapper.map(purchaseDTO, Purchase.class);

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

}
