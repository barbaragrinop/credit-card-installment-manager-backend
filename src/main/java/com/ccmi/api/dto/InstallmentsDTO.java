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
    private Map<String, Boolean> months; // Adicionando o campo months
    private CardDTO card;
}
