package com.ccmi.api.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter

public class PurchaseDTO implements Serializable {

    private Long id;
    
    private LocalDate date;

    private Integer installments;
    private String store;

    private Double value;

    private String productName;

    // private Card card;
}

