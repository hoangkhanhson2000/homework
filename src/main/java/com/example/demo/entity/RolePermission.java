package com.example.demo.entity;
import javax.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Data
@Entity
@Table(name = "ROLE_PERMISSION")
public class RolePermission {
    @Id
    @Type(type = "uuid-char")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Roles roles;

    private String permission;
}
