package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.GeneratedValue;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "cCode")
    private String code;

    @Column(name = "cName")
    private String name;
}
