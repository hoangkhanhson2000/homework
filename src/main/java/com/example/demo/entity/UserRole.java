package com.example.demo.entity;

import javax.persistence.*;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "USER_ROLE")
public class UserRole implements Serializable {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Serial
    private static final long serialVersionUID = 1L;

}
