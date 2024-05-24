package com.ccmi.api.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter

public class UserDTO implements Serializable {
    private String name;
    private String email;
    private String password;
    private LocalDate birth_date;
}


