package com.ccmi.api.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ccmi.api.dto.PurchaseDTO;
import com.ccmi.api.entity.Purchase;
import com.ccmi.api.entity.User;
import com.ccmi.api.service.UserService;


@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private UserService _userService;

    @Autowired
    private ModelMapper _modelMapper;

    @PostMapping
    public ResponseEntity<?> createPurchase(@RequestBody PurchaseDTO purchaseDTO) {

        

        // User user = _userService.findUserByEmail(purchaseDTO.getUser_email());

        // if(user == null) {
        //     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        // }


        // Purchase purchaseEntity = _modelMapper.map(purchaseDTO, Purchase.class);

        // purchaseEntity.set(user);
        



        return ResponseEntity.ok(purchaseDTO);
    }
    
}
