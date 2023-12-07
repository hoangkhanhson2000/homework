package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByExpireDateAfter(LocalDate date, Pageable pageable);
    List<Product> findByCategoryId(Long categoryId);
    @NotNull
    List<Product> findAll();

}
