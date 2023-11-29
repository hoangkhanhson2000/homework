package com.example.demo.repository;

import com.example.demo.entity.Roles;
import com.example.demo.entity.Users;
import com.example.demo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByUser(Users users);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user = :user AND ur.role = :role")
    void deleteByUserAndRole(Users user, Roles role);
}

