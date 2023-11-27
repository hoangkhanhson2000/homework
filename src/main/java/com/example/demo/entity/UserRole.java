package com.example.demo.entity;

import javax.persistence.*;

import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Data
@Entity
@Table(name = "USER_ROLE")
public class UserRole {

    @Id
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles roles;


}
