package com.example.demo.entity;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "USER_ROLE")
public class UserRole {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
