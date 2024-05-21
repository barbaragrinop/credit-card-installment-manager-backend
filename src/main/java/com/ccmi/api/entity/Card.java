package com.ccmi.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(
    name = "card", 
    uniqueConstraints = @UniqueConstraint(columnNames = { "email" }
))
@Entity(name = "Card")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")

public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "dueDate", columnDefinition = "DATE", nullable = false)
    private LocalDate dueDate;

    @Column(name = "cardBrand", nullable = false)
    private String cardBrand;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
