package com.example.demo.entity;
import javax.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "ROLE_PERMISSION")
public class RolePermission implements Serializable {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private String permission;

    @Serial
    private static final long serialVersionUID = 1L;

}
