package com.ccmi.api.dto;

import lombok.*;

@Getter
@Setter

public class CardDTO {
    private String name;
    private Integer dueDay;
    private String cardBrand;
    private String userEmail;
}
