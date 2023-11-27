package com.example.demo.entity;

import javax.persistence.*;

import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "ROLES")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    private String roleName;
    @OneToMany(mappedBy = "roles", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<RolePermission> permissions;
}
