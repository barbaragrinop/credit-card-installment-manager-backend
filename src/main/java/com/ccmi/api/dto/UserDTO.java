package com.ccmi.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;


@Getter
@Setter
@ToString
public class UserDTO implements Serializable {
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;
}


