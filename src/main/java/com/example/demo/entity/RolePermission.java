package com.example.demo.entity;
import javax.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "ROLE_PERMISSION")
public class RolePermission {
    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private String permission;
}
