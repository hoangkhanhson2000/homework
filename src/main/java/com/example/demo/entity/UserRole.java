package com.example.demo.entity;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Data
@Entity
@Table(name = "USER_ROLES")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Roles role;
}

