package com.example.demo.repository;

import com.example.demo.entity.Users;
import com.example.demo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser(Users users);
}

