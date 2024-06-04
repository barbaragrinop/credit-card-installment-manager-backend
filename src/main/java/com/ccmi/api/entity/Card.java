package com.ccmi.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Table(name = "card", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))

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

    //unique field

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "dueDay", nullable = false)
    private Integer dueDay;

    @Column(name = "cardBrand", nullable = false)
    private String cardBrand;

    
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Purchase> purchases;
}
