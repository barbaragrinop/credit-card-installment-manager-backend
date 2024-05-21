package com.ccmi.api.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "purchase")
@Entity(name = "Purchase")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")

public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "date", columnDefinition = "DATE", nullable = false)
    private LocalDate date;

    @Column(name = "installments", nullable = false)
    private Integer installments;

    @Column(name = "store", nullable = false)
    private String store;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "productName",  nullable = false)
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false) 
    private Card card;
}
