package com.example.demo.entity;
import javax.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "ROLE_PERMISSION")
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles roles;

    private String permission;
}
