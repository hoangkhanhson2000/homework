package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "pCode")
    private String code;

    @Column(name= "pName")
    private String name;

    @Column(name= "price")
    private double price = 0;

    @Column(name= "expDate")
    private LocalDate expireDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
