package com.example.demo.entity;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "ROLES")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String roleName;
}

