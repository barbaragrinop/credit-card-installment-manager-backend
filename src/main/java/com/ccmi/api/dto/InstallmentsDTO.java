package com.ccmi.api.dto;

import java.time.LocalDate;

import lombok.*;
import java.util.Map;

@Getter
@Setter

public class InstallmentsDTO extends PurchaseDTO {
    private Integer installmentsLeft;
    private LocalDate lastInstallmentDate;
    private Integer installmentsPaid;
    private String valuePerInstallment;
    private String valueLeft;
    private String valuePaid;
    private Map<String, Map<String, Object>> months; 
    private CardDTO card;
}
