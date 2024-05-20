package com.ccmi.api.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(
    name = "user", 
    uniqueConstraints = @UniqueConstraint(columnNames = { "email" }
))
@Entity(name = "User")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birthDate", columnDefinition = "DATE")
    private LocalDate dateOfBirth;

}
